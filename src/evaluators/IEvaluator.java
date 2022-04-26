package evaluators;

import graph.Edge;
import graph.Node;

public interface IEvaluator {
	public double getHX(Node current, Node end);
	public double getGX(Node current, Edge edge);
	public double getFX(Node current, Node neighbour, Node end, Edge edge);
}
