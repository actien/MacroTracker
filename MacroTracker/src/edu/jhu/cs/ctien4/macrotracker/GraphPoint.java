package edu.jhu.cs.ctien4.macrotracker;

public class GraphPoint {
	private int x;
	private int y;
	private String label;
	
	public GraphPoint(int x, int y, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getLabel() {
		return label;
	}
}
