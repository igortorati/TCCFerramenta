package graph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.InstanceCreator;

import ui.Constants;


public class Node implements Serializable, InstanceCreator<Node>{
	private transient double cost = 0;
	private transient double heuristicCost = 0;
	private transient double fCost = 0;
	private int column;
	private int row;
	private boolean walkable = true;
	private boolean open = true;
	private boolean visited = false;
	private boolean selected = false;
	private transient Node parent = null;
	private transient List<Edge> edges;
	
	private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	public Node(int column, int row) {
		super();
		this.column = column;
		this.row = row;
	}
	
	public Node() {
		super();
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	public void setSelected(boolean discovered) {
		this.selected = discovered;
		support.firePropertyChange(Constants.NodeChanged, null, null);
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
		support.firePropertyChange(Constants.NodeChanged, null, null);
	}
	public void setWalkable(boolean isWalkable) {
		boolean wasWalkable = this.getWalkable();
		if(wasWalkable != isWalkable) {
			this.walkable = isWalkable;
		}
	}
	
	public double getCost() {
		return cost;
	}
	
	public boolean getWalkable() {
		return walkable;
	}
	
	public boolean getVisited() {
		return visited;
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public List<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<Node>();
		for(Edge edge : edges) {
			if(!neighbours.contains(edge.getNeighbor(this))){
				neighbours.add(edge.getNeighbor(this));
			}
		}
		return neighbours;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Node: (" + getRow() + "," + getColumn() + "): " + edges.toString();
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return parent;
	}

	public void reset() {
		visited = false;
		selected = false;
		parent = null;
		cost = 0;
		edges = new ArrayList<Edge>();
	}

	public boolean getOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public double getHeuristicCost() {
		return heuristicCost;
	}

	public void setHeuristicCost(double heuristicCost) {
		this.heuristicCost = heuristicCost;
	}

	public double getFCost() {
		return fCost;
	}

	public void setFCost(double fCost) {
		this.fCost = fCost;
	}

	@Override
	public Node createInstance(Type type) {
		return new Node();
	}
}
