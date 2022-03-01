package ui;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graph.Matrix;

public class CanvasContainer extends JPanel{
	private JLabel canvasLabel;
	private JLabel canvasResult;
	private Canvas canvas;
	private double cost = 0;
	private int nodesVisited = 0;
	
	public CanvasContainer(String label, Canvas canvas) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		canvasLabel = new JLabel(label);
		canvasLabel.setAlignmentX(this.CENTER_ALIGNMENT);
		add(canvasLabel);
		
		this.canvas = canvas;
		add(canvas);
		
		canvasResult = new JLabel(generateResult());
		canvasResult.setAlignmentX(this.CENTER_ALIGNMENT);
		add(canvasResult);
		
		setVisible(true);
	}
	
	private String generateResult() {
		return "Cost: " + cost + "   Nodes Visited: " + nodesVisited;
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void setResults(double d, int nodesVisited) {
		cost = d;
		this.nodesVisited = nodesVisited;
		canvasResult.setText(generateResult());
		
	}

	public void setResultsNotFound() {
		canvasResult.setText("Caminho não encontrado!");
	}
	
	// colocar um eventListener do canvas aqui para quando houver a ação do mouse. E fazer o mesmo com a classe exterior a essa,
	//colocar um listener, e ela irá atualizar as matrizes e solicitar o repaint de todos os canvas.
}
