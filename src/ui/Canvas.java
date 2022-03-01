package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.BasicStroke;

import javax.swing.JComponent;

import com.google.gson.Gson;

import graph.Matrix;
import graph.Node;
import graph.Position;
import ui.Constants.Mode;
import ui.Constants.Pencil;

public class Canvas extends JComponent{
	private Matrix matrix;
	private Parameters parameters;
	private int[] margin; // top, right, botton, left, 
	
	public Canvas() {
		super();
		
		setPreferredSize(new Dimension(Constants.DEFAULT_CANVAS_SIZE, Constants.DEFAULT_CANVAS_SIZE));
		setMaximumSize(new Dimension(Constants.DEFAULT_CANVAS_SIZE, Constants.DEFAULT_CANVAS_SIZE));
		setMinimumSize(new Dimension(Constants.DEFAULT_CANVAS_SIZE, Constants.DEFAULT_CANVAS_SIZE));
		setSize(Constants.DEFAULT_CANVAS_SIZE, Constants.DEFAULT_CANVAS_SIZE);
		setVisible(true);
		margin = new int[4];
		margin[0] = 0;
		margin[1] = 0;
		margin[2] = 0;
		margin[3] = 0;
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		paintMatrix(g);
	}
	
	public Matrix getMatrix() {
		return matrix;
	}
	
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	public void paintMatrix(Graphics g) {
		int rows = matrix.getRows();
		int columns = matrix.getColumns();
		int cellSize = parameters.getCellSize();

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Node node = matrix.getNode(row, column);
				if (!node.getWalkable()) {
					g.setColor(Constants.NOT_WALKABLE_NODE_COLOR);
				} else if (node == matrix.getStartNode()) {
					g.setColor(Constants.START_NODE_COLOR);
				} else if (node == matrix.getEndNode()) {
					g.setColor(Constants.END_NODE_COLOR);
				} else if (node.getVisited()) {
					g.setColor(Constants.VISITED_NODE_COLOR);
				} else {
					continue;
				}
				int x = cellSize * column + margin[3];
				int y = cellSize * row + margin[0];
				g.fillRect(x, y, cellSize, cellSize);
			}
		}
		

		g.setColor(Constants.LINE_COLOR);
		for (int row = 0; row <= rows; row++) {
			int y = cellSize * row + margin[0];
			g.drawLine(margin[3], y, margin[1], y);
		}
		for (int column = 0; column <= columns; column++) {
			int x = cellSize * column + margin[3];
			g.drawLine(x, margin[0], x, margin[2]);
		}
		
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Node node = matrix.getNode(row, column);
				if(node.getSelected() && node.getParent() != null) {
					g.setColor(Constants.SELECTED_NODE_COLOR);
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(Math.min(5, cellSize / 2)));
					int halfCell = cellSize / 2;
					int x1 = cellSize * column + margin[3] + halfCell;
					int y1 = cellSize * row + margin[0] + halfCell;
					Node parent = node.getParent();
					int x2 = cellSize * parent.getColumn() + margin[3] + halfCell;
					int y2 = cellSize * parent.getRow() + margin[0] + halfCell;
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}

	public void matrixCellSizeUpdate() {
		Dimension dimension = calculateDimensions();
		updateMatrix();
	}

	private void updateMatrix() {
		int cellSize = parameters.getCellSize();
		int rows = (getHeight() - Constants.MARGIN * 2) / cellSize;
		int columns =(getWidth() - Constants.MARGIN * 2) / cellSize;
		margin[0] = (getHeight() - cellSize * rows)/2;
		margin[3] = (getWidth() - cellSize * columns)/2;
		margin[1] = margin[3] + cellSize * columns;
		margin[2] = margin[0] + cellSize * rows;
		matrix.setStart((Node) null);
		matrix.setEnd((Node) null);
		matrix.setDimensions(rows, columns);
		repaint();
	}

	private Dimension calculateDimensions() {
		int rows = (getHeight() - Constants.MARGIN * 2) / parameters.getCellSize();
		int columns = (getWidth() - Constants.MARGIN * 2) / parameters.getCellSize();
		return new Dimension(rows, columns);
	}
	
	private Node getNode(int x, int y) {
		int cellSize = parameters.getCellSize();
		if (x > margin[3] && y > margin[0] && x < margin[1] && y < margin[2]) {
			int row = (y - margin[0]) / cellSize;
			int col = (x - margin[3]) / cellSize;
			return matrix.getNode(row, col);
		}
		return null;
	}
	
	MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent aEvent) {
			if (parameters.isRunning()) {
				return;
			}
			Point position = aEvent.getPoint();
			Node node = getNode(position.x, position.y);
			
			if(parameters.getMode() == Mode.EDIT_MODE) {
				switch (parameters.getPencil()) {
					case OBSTACLE:
						if(node != null) {
							node.setWalkable(false);
						}
						break;
					case ERASER:
						if(node != null && node != matrix.getStartNode() && node != matrix.getEndNode()) {
							node.setWalkable(true);
						} else if(node == matrix.getStartNode()) {
							matrix.setStart((Node) null);
						}else if(node == matrix.getEndNode()) {
							matrix.setEnd((Node) null);
						}
						break;
				}
				firePropertyChange(Constants.MatrixEdited, null, null);
			}
			// necessário armazenar os pontos onde om ouse passou e desenhar uma reta entre eles, isso evita "buracos" entre o preenchimentodos das células.
		}

		@Override
		public void mouseReleased(MouseEvent aEvent) {
			Point position = aEvent.getPoint();
			Node node = getNode(position.x, position.y);
			
			if(parameters.getMode() == Mode.EDIT_MODE) {
				switch (parameters.getPencil()) {
					case OBSTACLE:
						if(node != null && node != matrix.getStartNode() && node != matrix.getEndNode()) {
							node.setWalkable(false);
						}
						break;
					case ERASER:
						if(node != null && node != matrix.getStartNode() && node != matrix.getEndNode()) {
							node.setWalkable(true);
						} else if(node == matrix.getStartNode()) {
							matrix.setStart((Node) null);
						}else if(node == matrix.getEndNode()) {
							matrix.setEnd((Node) null);
						}
						break;
					case START:
						if(node != null) {
							matrix.setStart(node);
						}
						break;
					case DESTINATION:
						if(node != null) {
							matrix.setEnd(node);
						}
						break;
				}
				firePropertyChange(Constants.MatrixEdited, null, null);
			}
		}
	};

	public void clearMatrix() {
		matrix.setDimensions(matrix.getRows(), matrix.getColumns());
		matrix.setStart((Node) null);
		matrix.setEnd((Node) null);
	}
	
	public SaveData getSaveData() {
		Gson gson = new Gson();
		Matrix baseMatrix = matrix.getBaseMatrix();
		SaveData saveData = new SaveData(baseMatrix, parameters);
		return saveData;
	}
}
