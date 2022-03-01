package graph;

public class Position {
	int row;
	int column;
	
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "row: " + row + " column: " + column;
	}
}
