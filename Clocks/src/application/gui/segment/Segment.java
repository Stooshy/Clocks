package application.gui.segment;

import javafx.scene.layout.Region;

class Segment extends Region
{
	private double x, y;
	public static final double defSegmentHeigths = 24;
	public static final double defSegmentWidth = 6;
	public static final double defRotSegmentHeights = defSegmentWidth;
	public static final double defRotSegmentWidth = defSegmentHeigths;


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
