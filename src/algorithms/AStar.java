package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import evaluators.Evaluator;
import graph.Edge;
import graph.Matrix;
import graph.Node;
import ui.Parameters;

public class AStar implements Algorithm{
	private PriorityQueue<Node> priorityQueue;
	private double totalCost = 0;
	private Parameters parameters;
	private Evaluator evaluator;
	
	@Override
	public boolean searchPath(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return false;
		if(start == null || end == null) return false;
		setup(matrix, start, end);
		while(!priorityQueue.isEmpty() && parameters.isRunning()){
			Node current = priorityQueue.remove();
			current.setVisited(true);
			if(current != end) {
				List<Edge> edges = current.getEdges();
				for(Edge edge : edges) {
					Node neighbour = edge.getNeighbor(current);
					double costToNode = current.getCost() + edge.getWeight();
					double temp = costToNode + evaluator.calculateHeuristic(neighbour, end); //g(x) + h(x)
					if(temp < neighbour.getCost()) {
						neighbour.setCost(costToNode);
						neighbour.setFCost(temp);
						neighbour.setParent(current);
						if(!neighbour.getVisited()) {
							priorityQueue.add(neighbour);
						}
					}
				}
			} else {
				totalCost = end.getCost();
				return true;
			}
		}
		return false;
	}

	private void setup(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return;
		priorityQueue = new PriorityQueue<Node>(new NodeComparatorFCost());
		evaluator = new Evaluator();
		for(int row = 0; row < matrix.getRows(); row++) {
			for(int column = 0; column < matrix.getColumns(); column++) {
				Node node = matrix.getNode(row, column);
				if(node.getWalkable()) {
					if(node == start) {
						node.setHeuristicCost(evaluator.calculateHeuristic(node, end));
						node.setFCost(0 + node.getHeuristicCost());
						priorityQueue.add(start);
					} else {
						node.setCost(Integer.MAX_VALUE);
					}
				}
			}
		}
	}

	@Override
	public void markPath(Node start, Node end) {
		if(!parameters.isRunning()) return;
		List<Node> path = new ArrayList<Node>();
		Node current = end;
		while(current.getParent() != null && parameters.isRunning()) {
			path.add(current);
			current = current.getParent();
		}
		path.add(start);
		for(int i = path.size() - 1; i >= 0 && parameters.isRunning(); i--) {
			path.get(i).setSelected(true);
		}
	}

	@Override
	public void buildGraph(Matrix matrix) {
		if(!parameters.isRunning()) return;
		for(int row = 0; row < matrix.getRows(); row++) {
			for(int column = 0; column < matrix.getColumns(); column++) {
				Node node = matrix.getNode(row, column);
				if(node != null && node.getWalkable()) {
					buildNodeEdges(matrix, node);
				}
			}
		}
	}

	@Override
	public void buildNodeEdges(Matrix matrix, Node node) {
		List<Edge> edges = new ArrayList<Edge>();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(!(i == 0 && j == 0)) {
					int neighborRow = node.getRow() + i;
					int neighborColumn = node.getColumn() + j;
					Node neighbor = matrix.getNode(neighborRow, neighborColumn);
					if(neighbor != null && neighbor.getWalkable()) {
						edges.add(new Edge(node, matrix.getNode(neighborRow, neighborColumn)));
					}
				}
			}
		}
		node.setEdges(edges);
	}


	@Override
	public int getNodesVisited(Matrix matrix) {
		int visited = 0;
		for(int row = 0; row < matrix.getRows(); row++) {
			for(int column = 0; column < matrix.getColumns(); column++) {
				if(matrix.getNode(row, column).getVisited()) {
					visited++;
				}
			}
		}
		return visited;
	}


	@Override
	public double getTotalCost() {
		return totalCost;
	}

	@Override
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
}

class NodeComparatorFCost implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getFCost() < n2.getFCost()) {
			return -1;
		} else if(n1.getFCost() > n2.getFCost()) {
			return 1;
		}
		if(n1.getCost() < n2.getCost()) {
			return -1;
		} else if (n1.getCost() > n2.getCost()) {
			return 1;
		}
		return 0;
	}
}