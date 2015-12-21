package application.gui.segment;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;

//@formatter:off
/**
 * 7 Segement Skin
 *              a
 * 			f   	b
 * 				g
 * 			e		c
 * 				d
 */
//@formatter:on
public class SevenSegmentsSkin extends SkinBase<SevenSegmentsControl> implements Skin<SevenSegmentsControl>
{
	public static final double MINIMUM_WIDTH = 10;
	public static final double MINIMUM_HEIGHT = 10;
	public static final double MAXIMUM_WIDTH = Segment.rotSegmentWidth + Segment.segmentWidth;
	public static final double MAXIMUM_HEIGHT = Segment.segmentHeigths * 2 + Segment.rotSegmentHeights;
	public static final double PREFERRED_WIDTH = Segment.rotSegmentWidth + Segment.segmentWidth;
	public static final double PREFERRED_HEIGHT = Segment.segmentHeigths * 2 + Segment.rotSegmentHeights;
	private final Pane pane = new Pane();
	protected final List<Segment> digits = new ArrayList<Segment>();


	public SevenSegmentsSkin(final SevenSegmentsControl CONTROL)
	{
		super(CONTROL);
		pane.setId("segment");
		init();
		buildPane();
		addListeners();
	}


	private void init()
	{
		if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0
				|| Double.compare(getSkinnable().getWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getHeight(), 0.0) <= 0)
		{
			if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0)
			{
				getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
			}
			else
			{
				getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0)
		{
			getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		}

		if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0)
		{
			getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
		}
	}


	private void addNewSegment(double x, double y, boolean rotate)
	{
		Segment svg = null;
		addShadowSegment(x, y, rotate);

		if (rotate)
		{
			svg = new Segment(x, y, "on-rot");
			svg.setPrefSize(Segment.defRotSegmentWidth, Segment.defRotSegmentHeights);
		}
		else
		{
			svg = new Segment(x, y, "on");
			svg.setPrefSize(Segment.defSegmentWidth, Segment.defSegmentHeigths);
		}
		digits.add(svg);
		pane.getChildren().add(svg);
	}


	private void addShadowSegment(double x, double y, boolean rotate)
	{
		Segment svg = null;
		if (rotate)
		{
			svg = new Segment(x, y, "off-rot");
			svg.setPrefSize(Segment.defRotSegmentWidth, Segment.defRotSegmentHeights);
		}
		else
		{
			svg = new Segment(x, y, "off");
			svg.setPrefSize(Segment.defSegmentWidth, Segment.defSegmentHeigths);
		}
		pane.getChildren().add(svg);
	}


	private void buildPane()
	{
		addNewSegment(Segment.rotSegmentHeights / 2, 0, true); // a
		addNewSegment(PREFERRED_WIDTH - Segment.segmentWidth, (Segment.rotSegmentHeights / 2), false); // b
		addNewSegment(PREFERRED_WIDTH - Segment.segmentWidth, PREFERRED_HEIGHT / 2, false); // c
		addNewSegment(Segment.segmentWidth / 2, PREFERRED_HEIGHT - Segment.defRotSegmentHeights, true); // d
		addNewSegment(0, PREFERRED_HEIGHT / 2, false); // f
		addNewSegment(0, Segment.rotSegmentHeights / 2, false); // e
		addNewSegment(Segment.segmentWidth / 2,
				PREFERRED_HEIGHT - Segment.defSegmentHeigths - Segment.defRotSegmentHeights, true); // g
		pane.getStyleClass().setAll("pane");
		getChildren().setAll(pane);
		resize();
	}


	private void handleNewValue(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<BitSet> newO = (SimpleObjectProperty<BitSet>) observable;
		BitSet set = (BitSet) newO.get();
		for (int idx = 0; idx < digits.size(); idx++)
		{
			digits.get(idx).setVisible(set.get(idx));
		}
	}


	private void addListeners()
	{
		getSkinnable().addNewValueListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				handleNewValue(observable);
			}
		});

		getSkinnable().widthProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				resize();
			}
		});

		getSkinnable().heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				resize();
			}
		});
	}


	protected void resize()
	{
		double size = getSkinnable().getWidth() < getSkinnable().getHeight() ? getSkinnable().getWidth()
				: getSkinnable().getHeight();

		if (size == 0)
		{
			return;
		}

		double scaleX = getSkinnable().getWidth() / PREFERRED_WIDTH;
		double scaleY = getSkinnable().getHeight() / PREFERRED_HEIGHT;
		getSkinnable().setScaleY(scaleY);
		getSkinnable().setScaleX(scaleX);
		pane.setPrefSize(getSkinnable().getWidth(), getSkinnable().getHeight());
		pane.setTranslateX(-size / 2);
		pane.setTranslateY(-size);

	}

}
