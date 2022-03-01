package graph;

public class Edge{
	private Node n1;
	private Node n2;
	private float weight;
	
	public Edge(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
		if(n1.getRow() == n2.getRow() || n1.getColumn() == n2.getColumn()) {
			weight = 1.0f;
		} else {
			weight = 1.4142f; //approximate sqrt of 2
		}
	}
	
	public Node getNeighbor(Node n) {
		if(n == n1) {
			return n2;
		}
		if(n == n2) {
			return n1;
		}
		return null;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public Node getN1() {
		return n1;
	}
	
	public Node getN2() {
		return n2;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "N1: (" + n1.getRow() + "," + n1.getColumn() + ") " +
			"N2: (" + n2.getRow() + "," + n2.getColumn() + ") " +
			"Weight: " + getWeight();
	}
}
