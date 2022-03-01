package algorithms;
import graph.Matrix;
import graph.Node;
import ui.Parameters;

public interface Algorithm {
	public double getTotalCost();

	public boolean searchPath(Matrix matrix, Node start, Node end);
	
	public void markPath(Node end, Node start);
	
	public void buildGraph(Matrix matrix);
	
	void buildNodeEdges(Matrix matrix, Node node);
	
	public void setParameters(Parameters parameters);

	int getNodesVisited(Matrix matrix);
}
