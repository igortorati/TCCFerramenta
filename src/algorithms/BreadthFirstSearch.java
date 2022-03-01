package algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import graph.Edge;
import graph.Matrix;
import graph.Node;
import ui.Parameters;

public class BreadthFirstSearch implements Algorithm{
	private Matrix matrix;
	private List<Node> queue;
	private double totalCost;
	private Parameters parameters;

	@Override
	public boolean searchPath(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return false;
		setup();
		System.out.println("Breadth-first");
		queue = new LinkedList<Node>();
		start.setVisited(true);
		queue.add(start);
		
		while(queue.size() != 0 && parameters.isRunning()) {
			Node current = queue.remove(0);
			List<Edge> edges = current.getEdges();
			for(Edge edge : edges) {
				Node node = edge.getNeighbor(current);
				if(!node.getVisited() && node.getWalkable()) {
					node.setVisited(true);
					queue.add(node);
					node.setParent(current);
					node.setCost(current.getCost() + edge.getWeight());
					if(node == end) {
						totalCost = node.getCost();
						return true;
					}
				}
			}
		}
		return false;
	}

	private void setup() {
		totalCost = 0;
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
		if(!parameters.isRunning()) return;
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
