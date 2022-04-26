package evaluators;

import graph.Edge;
import graph.Node;

public class AStarEvaluator implements IEvaluator{

	@Override
	public double getFX(Node current, Node neighbour, Node end, Edge edge) {
		return (getHX(neighbour, end) + getGX(current, edge));
	}

	@Override
	public double getHX(Node neighbour, Node end) {
		double xpow = Math.pow(neighbour.getColumn() - end.getColumn(), 2);
		double ypow = Math.pow(neighbour.getRow() - end.getRow(), 2);
		return Math.sqrt(xpow + ypow);
	}

	@Override
	public double getGX(Node current, Edge edge) {
		if(edge == null)
			return 0;
		return (current.getCost() + edge.getWeight());
	}

}
