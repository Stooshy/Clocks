package application.gui.segment;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

public class SevenSegmentsDisplay implements TimeProvider
{
	private final List<SevenSegmentsControl> segments = new ArrayList<SevenSegmentsControl>();
	private GridPane sp;
	private TimeProvider timeProvider;


	/**
	 * Pane containing 6, 7-segements LCD
	 */
	public SevenSegmentsDisplay()
	{
		buildDisplayPane();
	}


	private void buildDisplayPane()
	{
		sp = new GridPane();
		for (int colIdx = 1; colIdx <= 8; colIdx++)
		{
			if (colIdx == 3 || colIdx == 6)
			{
				sp.addColumn(colIdx, createMarks());
			}
			else
			{
				final SevenSegmentsControl seg = new SevenSegmentsControl();
				segments.add(seg);
				seg.setOnMouseReleased(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						SevenSegmentsControl source = (SevenSegmentsControl) event.getSource();
						source.set(source.getDigit().nextNumber());
					}
				});
				sp.addColumn(colIdx, seg);
			}
		}
	}


	public Pane getPane()
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


	public int getSeconds()
	{
		return segments.get(4).getDigit().ordinal() * 10 + segments.get(5).getDigit().ordinal();
	}


	public int getMinutes()
	{
		return segments.get(2).getDigit().ordinal() * 10 + segments.get(3).getDigit().ordinal();
	}


	public int getHours()
	{
		return segments.get(0).getDigit().ordinal() * 10 + segments.get(1).getDigit().ordinal();
	}


	private void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		segments.get(4).set(SevenDigit.values()[b]);
		segments.get(5).set(SevenDigit.values()[a]);
	}


	private void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		segments.get(2).set(SevenDigit.values()[b]);
		segments.get(3).set(SevenDigit.values()[a]);
	}


	private void setHours(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		segments.get(0).set(SevenDigit.values()[b]);
		segments.get(1).set(SevenDigit.values()[a]);
	}


	public void setTime(int seconds, int minutes, int hours)
	{
		setSeconds(seconds);
		setMinutes(minutes);
		setHours(hours);
	}


	public void consumeTime()
	{
		setTime(timeProvider.getSeconds(), timeProvider.getMinutes(), timeProvider.getHours());
	}


	public void setTimeProvider(final TimeProvider provider)
	{
		this.timeProvider = provider;
	}


	private static SVGPath createMarks()
	{
		SVGPath marks = new SVGPath();
		marks.setContent("m 40,1047.3622 5,0 0,5 -5,0 z m 0,-10 5,0 0,5 -5,0 z");
		return marks;
	}

}
