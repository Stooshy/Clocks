package application.gui.segment;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

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
public class SevenSegments extends SkinBase<SevenSegmentsControl> implements Skin<SevenSegmentsControl>
{
	private static final double MINIMUM_WIDTH = 10;
	private static final double MINIMUM_HEIGHT = 10;
	private static final double MAXIMUM_WIDTH = 165;
	private static final double MAXIMUM_HEIGHT = 210;
	private static final double PREFERRED_WIDTH = 65;
	private static final double PREFERRED_HEIGHT = 110;
	private final StackPane pane = new StackPane();
	protected final List<SVGPath> shadows = new ArrayList<SVGPath>();
	protected final List<SVGPath> digits = new ArrayList<SVGPath>();
	private static String SHAPE = "m 54.877123,1052.3432 0,-10.2949 -4.877123,0 c 1.625755,3.4314 3.252151,6.863 4.877123,10.2949 z m 0.217693,0.019 0,-10.2949 4.877122,0 c -1.625754,3.4317 -3.25215,6.8628 -4.877122,10.2949 z M 60,1013.0061 l -9.987503,0 0,28.6553 9.987503,0 z m -5.108634,-10.6257 0,10.2947 -4.877123,0 c 1.625755,-3.4315 3.25215,-6.8629 4.877123,-10.2947 z m 0.217693,-0.019 0,10.2949 4.877122,0 c -1.625754,-3.4316 -3.25215,-6.863 -4.877122,-10.2949 z";


	public SevenSegments(final SevenSegmentsControl CONTROL)
	{
		super(CONTROL);
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


	private void addNewSegment(int x, int y, double rotate)
	{
		addShadowSegment(x, y, rotate);

		SVGPath svg = new SVGPath();
		svg.getStyleClass().setAll("on");
		svg.setContent(SHAPE);
		svg.setTranslateY(y);
		svg.setTranslateX(x);
		svg.setRotate(rotate);

		digits.add(svg);
		pane.getChildren().add(svg);
	}


	private void addShadowSegment(int x, int y, double rotate)
	{
		SVGPath svg = new SVGPath();
		svg.getStyleClass().setAll("off");
		svg.setContent(SHAPE);
		svg.setTranslateY(y);
		svg.setTranslateX(x);
		svg.setRotate(rotate);

		shadows.add(svg);
		pane.getChildren().add(svg);
	}


	private void buildPane()
	{
		int x = 23;
		int y = 24;
		addNewSegment(0, -2 * y, 90);
		addNewSegment(x, -y, 0);
		addNewSegment(x, y, 0);
		addNewSegment(0, 2 * y, 90);
		addNewSegment(-x, y, 0);
		addNewSegment(-x, -y, 0);
		addNewSegment(0, 0, 90);

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

		double width = getSkinnable().getWidth();
		double hight = getSkinnable().getHeight();

		if (hight < PREFERRED_HEIGHT)
		{
			setScaleY(hight / (PREFERRED_HEIGHT));
		}
		else
		{
			setScaleY(1);
		}

		if (width < PREFERRED_WIDTH)
		{
			setScaleX(width / PREFERRED_WIDTH);

		}
		else
		{
			setScaleX(1);
		}
	}


	protected void setScaleX(double scale)
	{
		digits.forEach(s -> s.setScaleX(scale * 1.8));
		shadows.forEach(s -> s.setScaleX(scale * 1.8));
		getSkinnable().setScaleX(scale);
	}


	protected void setScaleY(double scale)
	{
		digits.forEach(s -> s.setScaleY(scale * 1.8));
		shadows.forEach(s -> s.setScaleY(scale * 1.8));
		getSkinnable().setScaleY(scale);
	}
}
