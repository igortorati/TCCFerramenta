package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import algorithms.Algorithm;
import algorithms.BestFirstSearch;
import graph.Matrix;

public class MainScreen {
	
	
	public static void main(String[] args) {
		Orchestrator orchestrator = null;
		Parameters parameters = new Parameters();
		JFrame frame = new JFrame("TCC - Comparação de Algoritmos de Pathfinding.");
		frame.setResizable(false);
		
		JPanel container = new JPanel();
		container.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		container.setPreferredSize(new Dimension(1625, 975));
		
		List<Algorithm> algorithmsList = createAlgorithms(parameters);
		List<CanvasContainer> canvasContainerList = createCanvasList(parameters);
		populateContainer(container, orchestrator, parameters, canvasContainerList, algorithmsList);
		
		container.setVisible(true);
		frame.add(container);
		frame.pack();
		
		frame.setVisible(true);
	}
	
	public static Canvas createCanvas(Parameters parameters) {
		final Matrix matrix = new Matrix();
		final Canvas canvas = new Canvas();
		canvas.setParameters(parameters);
		canvas.setMatrix(matrix);
		matrix.setNodeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (parameters.getAnimationDelay() == 0) {
					return;
				}
				if(parameters.isRunning()) {
					try {
						canvas.repaint();
						Thread.sleep(parameters.getAnimationDelay());
					} catch (InterruptedException e) {
					}
				}
			}
		});
		return canvas;
	}
	
	public static ArrayList<CanvasContainer> createCanvasList(Parameters parameters) {
		ArrayList<CanvasContainer> canvasContainerList = new ArrayList<CanvasContainer>();
		for(String key : Constants.ALGORITHMS.keySet()) {
			canvasContainerList.add(new CanvasContainer(Constants.ALGORITHMS.get(key), createCanvas(parameters)));
		}
		return canvasContainerList;
	}
	
	public static ArrayList<Algorithm> createAlgorithms(Parameters parameters) {
		ArrayList<Algorithm> algorithmsList = new ArrayList<Algorithm>();
		try {
			for(String key : Constants.ALGORITHMS.keySet()) {
				Algorithm algorithm = null;
				System.out.println("K: " + key);
				if(Class.forName("algorithms." + key) != null) {
					algorithm = (Algorithm) Class.forName("algorithms." + key).newInstance();
					algorithm.setParameters(parameters);
				}
				
				algorithmsList.add(algorithm);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return algorithmsList;
	}
	
	public static void populateContainer(JPanel container, Orchestrator orchestrator, 
			Parameters parameters, List<CanvasContainer> canvasContainerList,
			List<Algorithm> algorithmsList) {
		ConfigurationPanel configurationPanel = new ConfigurationPanel(parameters);
		RightPanel rightPanel = new RightPanel(parameters, canvasContainerList);
		
		orchestrator = new Orchestrator(configurationPanel, rightPanel, parameters);
		
		for(int i = 0; i < Constants.ALGORITHMS.keySet().size(); i++) {
			orchestrator.addPathfindingInfo(canvasContainerList.get(i).getCanvas(), algorithmsList.get(i), canvasContainerList.get(i));
		}
		
		container.add(configurationPanel);
		container.add(Box.createHorizontalStrut(5));
		container.add(rightPanel);
	}

}
