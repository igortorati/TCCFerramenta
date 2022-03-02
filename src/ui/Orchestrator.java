package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import algorithms.Algorithm;
import graph.Matrix;
import graph.Node;
import java.awt.image.BufferedImage;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

public class Orchestrator {
	private List<PathfindingInfo> pathfindingInfos;
	private ConfigurationPanel configurationPanel;
	private Parameters parameters;
	private JPanel panel;
	
	public Orchestrator(ConfigurationPanel configurationPanel, JPanel panel, Parameters parameters) {
		pathfindingInfos = new ArrayList<PathfindingInfo>();
		this.configurationPanel = configurationPanel;
		this.configurationPanel.addPropertyChangeListener(configurationListener);
		this.parameters = parameters;
		this.panel = panel;
	}
	
	private void copy(Matrix source, Matrix destination) {
		int rows = source.getRows();
		int columns = source.getColumns();
		destination.setDimensions(rows, columns);
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Node sourceNode = source.getNode(row, column);
				Node destinationNode = destination.getNode(row, column);
				destinationNode.setWalkable(sourceNode.getWalkable());
			}
		}
		if(source.getStartNode() != null) {
			destination.setStart(destination.getNode(source.getStartPosition()));
		} else {
			destination.setStart((Node) null);
		}
		if(source.getEndNode() != null) {
			destination.setEnd(destination.getNode(source.getEndPosition()));
		} else {
			destination.setEnd((Node) null);
		}
	}
	
	private PropertyChangeListener matrixEditedListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			stopRunning();
			Canvas src = (Canvas)evt.getSource();
			for (PathfindingInfo pi : pathfindingInfos) {
				pi.canvas.getMatrix().reset();
				if (pi.canvas != src) {
					copy(src.getMatrix(), pi.canvas.getMatrix());
				}
				pi.canvas.repaint();
			}
		}
	};
	
	private PropertyChangeListener configurationListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(Constants.SaveResults.equals(evt.getPropertyName()) != true) stopRunning();
			if (Constants.StartRequested.equals(evt.getPropertyName())) {
				System.out.println("Starting...");
				for (PathfindingInfo pi : pathfindingInfos) {
					pi.canvas.getMatrix().reset();
					pi.canvas.repaint();
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							System.out.println("Building Graph");
							pi.algorithm.buildGraph(pi.canvas.getMatrix());
							boolean pathFound = pi.algorithm.searchPath(pi.canvas.getMatrix(), pi.canvas.getMatrix().getStartNode(), pi.canvas.getMatrix().getEndNode());
							if(pathFound && parameters.isRunning()) {
								pi.algorithm.markPath(pi.canvas.getMatrix().getStartNode(), pi.canvas.getMatrix().getEndNode());
								pi.canvasContainer.setResults(pi.algorithm.getTotalCost(), pi.algorithm.getNodesVisited(pi.canvas.getMatrix()));
							} else {
								pi.canvasContainer.setResultsNotFound();
							}
						}
					};
					Thread thread = new Thread(runnable);
					pi.thread = thread;
				}
				stopRunning();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parameters.setRunning(true);
				for (PathfindingInfo pi : pathfindingInfos) {
					pi.thread.start();
				}
			}
			if (Constants.CellSizeChanged.equals(evt.getPropertyName())) {
				System.out.println("Size Changed...");
				for(PathfindingInfo pi : pathfindingInfos) {
					pi.canvas.matrixCellSizeUpdate();
				}
			}
			if (Constants.ClearMatrix.equals(evt.getPropertyName())) {
				System.out.println("Size Changed...");
				for(PathfindingInfo pi : pathfindingInfos) {
					pi.canvas.clearMatrix();
					pi.canvas.repaint();
				}
			}
			if (Constants.SaveMatrix.equals(evt.getPropertyName())) {
				String filePath = ((String) evt.getNewValue()).replace("\\", "\\\\");
				if(filePath == null) {
					return;
				}
				saveToFile(filePath);
			}
			if (Constants.LoadMatrix.equals(evt.getPropertyName())) {
				String filePath = ((String) evt.getNewValue()).replace("\\", "\\\\");
				if(filePath == null) {
					return;
				}
				populateLoadedData(readFromFile(filePath));
			}
			if (Constants.SaveResults.equals(evt.getPropertyName())) {
				String completePath = generatePath();
				saveImage(completePath);
				
				System.out.println("Result saved to: " + completePath);
				
				saveJson(completePath);
			}
		}
	};
	
	private void saveJson(String completePath) {
		List<ResultJson> results = new ArrayList<ResultJson>();
		for(PathfindingInfo pi : pathfindingInfos) {
			String algorithm = Constants.ALGORITHMS.get(pi.algorithm.getClass().getSimpleName());
			Double cost = pi.algorithm.getTotalCost();
			int visitedNodes = pi.algorithm.getNodesVisited(pi.canvas.getMatrix());
			ResultJson result = new ResultJson(algorithm, cost, visitedNodes);
			results.add(result);
		}
		
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(completePath + "\\\\results.json"), Charset.forName("UTF-8"))) {
            writer.write(gson.toJson(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void saveImage(String completePath) {
		new File(completePath).mkdirs();
		
		Component comp = panel.getRootPane().getComponents()[1];
		Dimension screenSize = new Dimension(comp.getWidth(), comp.getHeight());
		Rectangle screenRectangle = new Rectangle(screenSize);
		BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
		panel.paint(img.getGraphics());
		try {
			ImageIO.write(img, "png", new File(completePath + "\\\\image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generatePath() {
		String path = ((String) System.getProperty("user.dir").replace("\\", "\\\\"));
		SimpleDateFormat dateFormat = (new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"));
		String dirName = dateFormat.format(new Timestamp(System.currentTimeMillis()));
		return path + "\\\\" + dirName;
	}
	
	private void populateLoadedData(SaveData loadedData) {
		System.out.println(loadedData.parameters.getCellSize());
		parameters.setCellSize(loadedData.parameters.getCellSize());
		configurationPanel.updateCellSize();
		for(PathfindingInfo pi : pathfindingInfos) {
			pi.canvas.matrixCellSizeUpdate();
		}
		
		Matrix loadedMatrix = new Matrix();
		loadedMatrix.setDimensions(loadedData.matrix.getRows(), loadedData.matrix.getColumns());
		
		for(int row = 0; row < loadedMatrix.getRows(); row++) {
			for(int column = 0; column < loadedMatrix.getColumns(); column++) {
				Node node = loadedMatrix.getNode(row, column);
				node.setWalkable(loadedData.matrix.getNode(row, column).getWalkable());
			}
		}
		
		loadedMatrix.setStart(loadedMatrix.getNode(loadedData.matrix.getStartPosition()));
		loadedMatrix.setEnd(loadedMatrix.getNode(loadedData.matrix.getEndPosition()));
				
		
		for(PathfindingInfo pi : pathfindingInfos) {
			pi.canvas.setParameters(parameters);
			pi.canvas.repaint();
			copy(loadedMatrix, pi.canvas.getMatrix());
			pi.canvas.repaint();
		}
	}
	
	private SaveData readFromFile(String filePath) {
		SaveData savedData = null;
		try (InputStreamReader freader = new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8"))) {
            JsonReader reader = new JsonReader(freader);
            return new Gson().fromJson(reader, SaveData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	private void saveToFile(String filePath) {
		System.out.println("Saving Matrix to filepath " + filePath);
		Canvas canvas = pathfindingInfos.get(0).canvas;
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("UTF-8"))) {
            writer.write(new Gson().toJson(canvas.getSaveData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void addPathfindingInfo(Canvas canvas, Algorithm algorithm, CanvasContainer canvasContainer) {
		PathfindingInfo pathfindingInfo = new PathfindingInfo();
		pathfindingInfo.canvas = canvas;
		pathfindingInfo.algorithm = algorithm;
		pathfindingInfo.canvasContainer = canvasContainer;
		canvas.addPropertyChangeListener(Constants.MatrixEdited, matrixEditedListener);
		pathfindingInfos.add(pathfindingInfo);
		canvas.matrixCellSizeUpdate();
	}
	
	private void stopRunning() {
		System.out.println("Stop running");
		parameters.setRunning(false);
		for (PathfindingInfo pi : pathfindingInfos) {
			if(pi.thread != null) {
				pi.thread.interrupt();
				try {
					pi.thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			pi.canvas.getMatrix().reset();
			pi.canvas.repaint();
		}
	}
}

class PathfindingInfo{
	Canvas canvas;
	CanvasContainer canvasContainer;
	Algorithm algorithm;
	Thread thread;
}

class ResultJson {
	String algorithm;
	Double cost;
	int visitedNodes;
	
	ResultJson(String algorithm, Double cost, int visitedNodes) {
		this.algorithm = algorithm;
		this.cost = cost;
		this.visitedNodes = visitedNodes;
	}
}
