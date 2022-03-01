package evaluators;

import graph.Edge;
import graph.Node;

public class Evaluator implements IEvaluator{

	@Override
	public double calculateHeuristic(Node current, Node end) {
		double xpow = Math.pow(current.getColumn() - end.getColumn(), 2);
		double ypow = Math.pow(current.getRow() - end.getRow(), 2);
		return Math.sqrt(xpow + ypow);
	}

	@Override
	public float calculateCost(Node current, Edge edge, Node start, Node end) {
		// TODO Auto-generated method stub
		return 0;
	}

}
