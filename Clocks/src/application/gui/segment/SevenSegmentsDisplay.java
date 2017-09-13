package application.gui.segment;

import java.util.ArrayList;
import java.util.List;

import application.ObserveableTimeProvider;
import application.gui.Updateable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class SevenSegmentsDisplay extends HBox implements Updateable
{
	private final List<SevenDigitsHandler> segments = new ArrayList<SevenDigitsHandler>();
	protected ObserveableTimeProvider provider;
	private static double PREFERRED_WIDTH = SevenSegmentsSkin.PREFERRED_WIDTH * 8;
	private static double PREFERRED_HEIGHT = SevenSegmentsSkin.PREFERRED_HEIGHT + 10;
	private double MAXIMUM_WIDTH = PREFERRED_WIDTH * 4d;
	private double MAXIMUM_HEIGH = PREFERRED_HEIGHT * 4d;
	private static double MINIMUM_HEIGHT = PREFERRED_HEIGHT;
	private static double MINIMUM_WIDTH = -1;


	/**
	 * Hours, minutes, seconds: 00:00:00
	 */
	public SevenSegmentsDisplay(ObserveableTimeProvider timeProvider)
	{
		this();
		provider = timeProvider;

		provider.addMinutesListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				setMinutes(((SimpleIntegerProperty) observable).intValue());
			}
		});
		provider.addSecondsInvalidationListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setSeconds(((SimpleIntegerProperty) observable).intValue());
			}
		});
		provider.addHoursListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				setHours(((SimpleIntegerProperty) observable).intValue());
			}
		});
		widthProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				if (((ReadOnlyDoubleProperty) observable).get() == 0d)
				{
					return;
				}
				double hi = ((ReadOnlyDoubleProperty) observable).get();
				double scaleX = hi / PREFERRED_WIDTH;
				setScaleX(scaleX);
			}
		});
	}


	SevenSegmentsDisplay()
	{
		setId("clockpane");
		init();
		setAlignment(Pos.CENTER);
		setSpacing(5);
		setPadding(new Insets(5, 5, 5, 5));
		buildDisplayPane();
	}


	private void init()
	{
		if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0
				|| Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0)
		{
			if (getPrefWidth() > 0 && getPrefHeight() > 0)
			{
				setPrefSize(getPrefWidth(), getPrefHeight());
			}
			else
			{
				setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0)
		{
			setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		}

		if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0)
		{
			setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGH);
		}
	}


	private void buildDisplayPane()
	{
		for (int colIdx = 0; colIdx <= 7; colIdx++)
		{
			if (colIdx == 2 || colIdx == 5)
			{
				getChildren().add(createMarks());
			}
			else
			{
				final SevenSegmentsControl seg = new SevenSegmentsControl("segmentskin");
				segments.add(seg);
				getChildren().add(seg);
			}
		}
	}


	public int getMilliSeconds()
	{
		return 0;
	}


	protected void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		setSegmenet(4, b);
		setSegmenet(5, a);
	}


	protected void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		setSegmenet(2, b);
		setSegmenet(3, a);
	}


	protected void setHours(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		setSegmenet(0, b);
		setSegmenet(1, a);
	}


	private Region createMarks()
	{
		Region svg = new Region();
		svg.setId("marks");
		svg.getStyleClass().setAll("marks");
		svg.setPrefSize(5, 15);
		return svg;
	}


	protected void setSegmenet(int idx, int value)
	{
		getSegment(idx).set(SevenDigit.values()[value]);
	}


	protected SevenDigitsHandler getSegment(int idx)
	{
		return segments.get(idx);
	}


	@Override
	public void pauseUpdate()
	{
		provider.pauseUpdate();
	}


	@Override
	public void startUpate()
	{
		provider.startUpate();
	}


	@Override
	public boolean isRunning()
	{
		return provider.isRunning();
	}
}
