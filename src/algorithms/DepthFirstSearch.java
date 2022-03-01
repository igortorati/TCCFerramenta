package algorithms;

import graph.Matrix;
import graph.Node;
import ui.Parameters;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import graph.Edge;

public class DepthFirstSearch implements Algorithm{
	private Matrix matrix;
	private Stack<Node> stack;
	private double totalCost;
	private Parameters parameters;
	
	@Override
	public boolean searchPath(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return false;
		if(start == null || end == null) return false;
		setup();
		this.matrix = matrix;
		stack = new Stack<Node>();
		stack.add(start);
		
		try {
			while(stack.size() != 0 && parameters.isRunning()) {
				Node current = stack.pop();
				if(current.getVisited()) continue;
				current.setVisited(true);
				if(current != end) {
					List<Edge> edges = current.getEdges();
					for(Edge edge : edges) {
						Node neighbour = edge.getNeighbor(current);
						neighbour.setCost(current.getCost() + edge.getWeight());
						if(!neighbour.getVisited() && neighbour.getWalkable()){
							neighbour.setParent(current);
							stack.push(neighbour);
						}
					}
				} else {
					totalCost = end.getCost();
					return true;
				}
			}
		} catch (EmptyStackException e) {
			return false;
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
