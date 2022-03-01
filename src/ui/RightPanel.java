package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import graph.Matrix;
import graph.Position;

public class RightPanel extends JPanel{
	private List<CanvasContainer> canvasContainerList;
	private Parameters parameters;
	
	public RightPanel(Parameters parameters, List<CanvasContainer> canvasContainerList)  {
		super();
		this.parameters = parameters;
		setPreferredSize(new Dimension(1370, 975)); // won't work properly with other numbers of rows and columns
		
		this.canvasContainerList = canvasContainerList;
		for(CanvasContainer c : canvasContainerList) {
			add(c);
		}
		
		setVisible(true);
	}
}
