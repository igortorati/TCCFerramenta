package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import graph.BinaryHeap;
import graph.Edge;
import graph.Matrix;
import graph.Node;
import ui.Parameters;

public class Dijkstra implements Algorithm{
	private Matrix matrix;
	private PriorityQueue<Node> priorityQueue;
	private double totalCost = 0;
	private Parameters parameters;
	
	@Override
	public boolean searchPath(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return false;
		setup(matrix, start);
		while(!priorityQueue.isEmpty() && parameters.isRunning()){
			Node current = priorityQueue.remove();
			if(!current.getVisited()) {
				current.setVisited(true);
				if(current != end) {
					List<Edge> edges = current.getEdges();
					for(Edge edge : edges) {
						double temp = current.getCost() + edge.getWeight();
						Node neighbour = edge.getNeighbor(current);
						if(temp < neighbour.getCost()) {
							neighbour.setCost(temp);
							neighbour.setParent(current);
							priorityQueue.add(neighbour);
						}
					}
				} else {
					totalCost = end.getCost();
					return true;
				}
			}
		}
		return false;
	}

	private void setup(Matrix matrix, Node start) {
		if(!parameters.isRunning()) return;
		priorityQueue = new PriorityQueue<Node>(new NodeComparator());
		for(int row = 0; row < matrix.getRows(); row++) {
			for(int column = 0; column < matrix.getColumns(); column++) {
				Node node = matrix.getNode(row, column);
				if(node.getWalkable()) {
					if(node == start) {
						node.setCost(0);
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

class NodeComparator implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getCost() < n2.getCost()) {
			return -1;
		} else if(n1.getCost() > n2.getCost()) {
			return 1;
		}
		return 0;
	}
}