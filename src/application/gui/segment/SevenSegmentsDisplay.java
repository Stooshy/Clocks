package application.gui.segment;

import java.util.ArrayList;
import java.util.List;

import application.gui.ScreenNode;
import application.gui.TimeProvider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class SevenSegmentsDisplay implements TimeProvider, TimeConsumer, ScreenNode
{
	private final List<SevenDigitsHandler> segments = new ArrayList<SevenDigitsHandler>();
	private final HBox sp;
	protected TimeProvider timeProvider;


	/**
	 * Hours, minutes, seconds: 00:00:00
	 */
	public SevenSegmentsDisplay(TimeProvider timeProvider)
	{
		sp = new HBox();
		sp.setId("clockpane");
		sp.setAlignment(Pos.CENTER);
		sp.setSpacing(5);
		sp.setPadding(new Insets(5, 5, 5, 5));
		sp.setPrefHeight(115);
		sp.setPrefWidth(225);
		buildDisplayPane();
		setTimeProvider(timeProvider);

		sp.widthProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layoutPane();
			}
		});

		sp.heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layoutPane();
			}
		});
	}


	private void layoutPane()
	{
		double scaleX = sp.getWidth() / 220;
		double scaleY = sp.getHeight() / 75;
		if (scaleX / 2 > 1 || scaleY / 2 > 1)
			return;

		for (Node pane : sp.getChildren())
		{
			if (pane.getId() != null)
			{
				pane.setScaleX(scaleX / 2);
				pane.setScaleY(scaleY / 2);
				pane.setTranslateY(5);
			}
		}
	}


	private void buildDisplayPane()
	{
		for (int colIdx = 0; colIdx <= 7; colIdx++)
		{
			if (colIdx == 2 || colIdx == 5)
			{
				sp.getChildren().add(createMarks());
			}
			else
			{
				final SevenSegmentsControl seg = new SevenSegmentsControl("segmentskin");
				segments.add(seg);
				seg.setOnMouseReleased(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						SevenSegmentsControl seg = (SevenSegmentsControl) event.getSource();
						if (event.getButton() == MouseButton.PRIMARY)
							seg.count(true);
						else
							seg.count(false);
					}
				});
				sp.getChildren().add(seg);
			}
		}
	}


	public Pane getNode()
	{
		return sp;
	}


	public void setTime(SevenDigit... values)
	{
		int idx = 0;
		for (SevenDigit value : values)
		{
			segments.get(idx++).set(value);
		}
	}


	public int getMilliSeconds()
	{
		return 0;
	}


	public int getSeconds()
	{
		return getSegment(4).getValueDisplayed() * 10 + getSegment(5).getValueDisplayed();
	}


	public int getMinutes()
	{
		return getSegment(2).getValueDisplayed() * 10 + getSegment(3).getValueDisplayed();
	}


	public int getHours()
	{
		return getSegment(0).getValueDisplayed() * 10 + getSegment(1).getValueDisplayed();
	}


	private void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(4).set(SevenDigit.values()[b]);
		getSegment(5).set(SevenDigit.values()[a]);
	}


	private void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(2).set(SevenDigit.values()[b]);
		getSegment(3).set(SevenDigit.values()[a]);
	}


	private void setHours(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(0).set(SevenDigit.values()[b]);
		getSegment(1).set(SevenDigit.values()[a]);
	}


	public void consumeTime()
	{
		setSeconds(timeProvider.getSeconds());
		setMinutes(timeProvider.getMinutes());
		setHours(timeProvider.getHours());
	}


	public void setTimeProvider(TimeProvider provider)
	{
		this.timeProvider = provider;
	}


	private Region createMarks()
	{
		Region svg = new Region();
		svg.setId("marks");
		svg.getStyleClass().setAll("marks");
		svg.setPrefSize(5, 15);
		svg.setTranslateY(0);
		svg.setTranslateX(0);
		return svg;
	}


	protected SevenDigitsHandler getSegment(int idx)
	{
		return segments.get(idx);
	}

}
