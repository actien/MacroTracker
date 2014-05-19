package edu.jhu.cs.ctien4.macrotracker;

/**
 * This is a data class used to store the points that
 * will be used by the GraphView library to generate the graph. This class
 * takes an additional parameter other than the x and y coordinates which is
 * the label that the GraphView library should use to label the points.
 */
public class GraphPoint {
	private int x;
	private int y;
	private String label;

    /**
     * Constructor for the GraphPoint
     */
	public GraphPoint(int x, int y, String label) {
		this.x = x;
		this.y = y;
		this.label = label;
	}

    /**
     * Get the x value associated with the GraphPoint.
     */
	public int getX() {
		return x;
	}

    /**
     * Get the y value associated with the GraphPoint.
     */
	public int getY() {
		return y;
	}

    /**
     * Get the label associated with the GraphPoint.
     */
	public String getLabel() {
		return label;
	}
}
