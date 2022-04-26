package ui;

import ui.Constants.Mode;
import ui.Constants.Pencil;

public class Parameters{
	private int cellSize = 15;
	private int animationDelay = 50;
	private boolean running = false;
	private Mode mode = Mode.EDIT_MODE;
	private Pencil pencil = Pencil.OBSTACLE;
	protected String aStarHeuristic;
	
	public int getAnimationDelay() {
		return animationDelay;
	}
	
	public void setAnimationDelay(int animationDelay) {
		this.animationDelay = animationDelay;
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean setRunning(boolean running) {
		return this.running = running;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Pencil getPencil() {
		return pencil;
	}
	
	public void setPencil(Pencil pencil) {
		this.pencil = pencil;
	}
	
	public String getAStarHeuristic() {
		return aStarHeuristic;
	}
	
	public void setAStarHeuristic(String aStarHeuristic) {
		this.aStarHeuristic = aStarHeuristic;
	}
	
	@Override
	public String toString() {
		return "cellSize: " + cellSize +
				"\n animationDelay: " + animationDelay +
				"\n running: " + running +
				"\n mode: " + mode +
				"\n pencil: " + pencil + "\n";
	}
}
