package evaluators;

import graph.Edge;
import graph.Node;

public interface IEvaluator {
	public double calculateHeuristic(Node current, Node end);
	public float calculateCost(Node current, Edge edge, Node start, Node end);
}
