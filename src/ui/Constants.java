package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String StartRequested = "Start";
	public static final String StopRequested = "Stop";
	public static final String MatrixEdited = "Matrix Edited";
	public static final String CellSizeChanged = "Cell Size Changed";
	public static final String ClearMatrix = "Clear Matrix";
	protected static final String SaveMatrix = "Save Matrix";
	protected static final String LoadMatrix = "Load Matrix";
	protected static final String SaveResults = "Save Results";
	public static final Map<String, String> ALGORITHMS = new HashMap<String, String> () {{
		put("Dijkstra","Dijkstra");
		put("BestFirstSearch", "Best-first Search");
		put("DepthFirstSearch", "Depth-first Search");
		put("BreadthFirstSearch", "Breadth-first Search");
		put("AStar","A*");
	}};
	public static final int DEFAULT_CANVAS_SIZE = 450;
	public static final String NodeChanged = "Node Changed";
	
	public final static Color LINE_COLOR           = new Color(150, 150, 200);
	public final static Color NOT_WALKABLE_NODE_COLOR  = new Color(0, 0, 0);
	public final static Color VISITED_NODE_COLOR   = new Color(155, 150, 250);
	public final static Color SELECTED_NODE_COLOR  = new Color(255, 255, 0);
	public final static Color START_NODE_COLOR     = new Color(0, 255, 0);
	public final static Color END_NODE_COLOR       = new Color(255, 0, 0);

	public final static Stroke STROKE = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 1.0f, new float[]{1}, 0);
	public final static int MARGIN = 10;


	public static enum Mode {
		EDIT_MODE, DEFINE_START, DEFINE_DESTINATION
	};
	
	public static enum Pencil {
		OBSTACLE, ERASER, START, DESTINATION
	};
}
