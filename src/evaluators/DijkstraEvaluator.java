package evaluators;

import graph.Edge;
import graph.Node;

public class DijkstraEvaluator implements IEvaluator{
	@Override
	public double getFX(Node current, Node neighbour, Node end, Edge edge) {
		return getGX(current, edge);
	}

	@Override
	public double getHX(Node current, Node end) {
		return 0;
	}

	@Override
	public double getGX(Node current, Edge edge) {
		if(edge == null)
			return 0;
		return current.getCost() + edge.getWeight();
	}
}
