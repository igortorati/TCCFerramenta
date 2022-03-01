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

public class BestFirstSearch implements Algorithm{
	private PriorityQueue<Node> priorityQueue;
	private double totalCost = 0;
	private Parameters parameters;
	private Evaluator evaluator;
	
	@Override
	public boolean searchPath(Matrix matrix, Node start, Node end) { //Terminar
		if(!parameters.isRunning()) return false;
		if(start == null || end == null) return false;
		setup(matrix, start, end);
		while(!priorityQueue.isEmpty() && parameters.isRunning()){
			Node current = priorityQueue.remove();
			current.setVisited(true);
			List<Edge> edges = current.getEdges();
			for(Edge edge : edges) {
				Node neighbour = edge.getNeighbor(current);
				if(!neighbour.getVisited()) {
					neighbour.setParent(current);
					neighbour.setCost(current.getCost() + edge.getWeight());
					if(neighbour == end) {
						neighbour.setVisited(true);
						totalCost = neighbour.getCost();
						return true;
					} else {
						double temp = evaluator.calculateHeuristic(neighbour, end);
						neighbour.setFCost(temp);
						if(!priorityQueue.contains(neighbour)) {
							priorityQueue.add(neighbour);
						}
					}
				}
				
			}
		}
		return false;
	}

	private void setup(Matrix matrix, Node start, Node end) {
		if(!parameters.isRunning()) return;
		evaluator = new Evaluator();
		priorityQueue = new PriorityQueue<Node>(new NodeComparatorFCost());
		start.setCost(0);
		priorityQueue.add(start);
		start.setFCost(evaluator.calculateHeuristic(start, end));
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