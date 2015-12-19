package application.gui.segment;

import javafx.scene.layout.Region;

public class Segment extends Region
{
	private double x, y;
	public static final double segmentHeigths = 50;
	public static final double segmentWidth = 10;
	public static final double rotSegmentHeights = 10;
	public static final double rotSegmentWidth = 50;
	public static final double defRotSegmentHeights = 10;
	public static final double defRotSegmentWidth = 50;
	public static final double defSegmentHeigths = 50;
	public static final double defSegmentWidth = 10;
	
	public Segment(double x, double y, String css)
	{
		this.x = x;
		this.y = y;
		getStyleClass().setAll(css);
		setTranslateY(y);
		setTranslateX(x);
	}


	public double getX()
	{
		return x;
	}


	public double getY()
	{
		return y;
	}
}
