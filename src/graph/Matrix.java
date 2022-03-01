package graph;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

import ui.Constants;

public class Matrix implements Serializable{
	private int rows;
	private int columns;
	private Node start;
	private Node end;
	private Node[][] matrix;
	private PropertyChangeListener nodeListener;
	
	public Matrix() {
	}
	
	public void setDimensions(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		matrix = new Node[columns][rows];
		for(int column = 0; column < columns; column++) {
			for(int row = 0; row < rows; row++) {
				matrix[column][row] = new Node(column, row);
				if(nodeListener != null) {
					matrix[column][row].addPropertyChangeListener(
						Constants.NodeChanged,
						nodeListener);
				}
			}
		}
	}
	
	public void setNodeWalkable(Position pos, boolean bool) {
		matrix[pos.column][pos.row].setWalkable(bool);
	}
	
	public void setStart(Node start) {
		if(start != null) {
			start.setWalkable(true);
		}
		this.start = start;
	}
	
	public void setEnd(Node end) {
		if(end != null) {
			end.setWalkable(true);
		}
		this.end = end;
	}
	
	public void setStart(Position position) {
		setStart(getNode(position));
	}
	
	public void setEnd(Position position) {
		setEnd(getNode(position));
	}
	
	public void setNodeListener(PropertyChangeListener nodeListener) {
		this.nodeListener = nodeListener;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}
	
	public Node getNode(int row, int column) {
		if(row >= 0 && row < getRows() && column >= 0 && column < getColumns()) {
			return matrix[column][row];
		}
		return null;
	}
	
	public Node getNode(Position position) {
		return getNode(position.row, position.column);
	}
	
	public Node getStartNode() {
		return start;
	}
	
	public Node getEndNode() {
		return end;
	}
	
	public Position getStartPosition() {
		return start != null ? new Position(start.getRow(), start.getColumn()) : null;
	}
	
	public Position getEndPosition() {
		return end != null ? new Position(end.getRow(), end.getColumn()) : null;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				s += "|";
				Node n = getNode(row, column);
				if(!n.getWalkable()) {
					s += "O";
				}else if(n == getStartNode()) {
					s += "S";
				} else if(n == getEndNode()) {
					s += "E";
				} else if(n.getVisited()) {
					s += "V";
				} else {
					s += " ";
				}
			}
			s += "|\n";
		}
		return s;
	}

	public void reset() {
		for(int column = 0; column < columns; column++) {
			for(int row = 0; row < rows; row++) {
				matrix[column][row].reset();
			}
		}
	}

	public Matrix getBaseMatrix() {
		Matrix baseMatrix = new Matrix();
		baseMatrix.setDimensions(rows, columns);
		for(int row = 0; row < rows; row++) {
			for(int column = 0; column < columns; column++) {
				Node matrixNode = this.getNode(row, column);
				Node baseMatrixNode = baseMatrix.getNode(row, column);
				baseMatrixNode.setWalkable(matrixNode.getWalkable());
			}
		}
		baseMatrix.setStart(baseMatrix.getNode(this.getStartPosition()));
		baseMatrix.setEnd(baseMatrix.getNode(this.getEndPosition()));
		return baseMatrix;
	}
}
