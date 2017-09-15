package application.gui.segment;

import javafx.scene.layout.Region;

class Segment extends Region
{
	static final double defSegmentHeigths = 24;
	static final double defSegmentWidth = 6;
	static final double defRotSegmentHeights = defSegmentWidth;
	static final double defRotSegmentWidth = defSegmentHeigths;


	public Segment(double x, double y, String css)
	{
		getStyleClass().setAll(css);
		setTranslateY(y);
		setTranslateX(x);
	}
}
