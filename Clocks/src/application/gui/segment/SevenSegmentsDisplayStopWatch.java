package application.gui.segment;

import application.counter.Counter;
import application.counter.CounterMode;
import application.counter.TimeCounter;
import application.gui.Updateable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class SevenSegmentsDisplayStopWatch extends SevenSegmentsDisplay implements Counter, Updateable
{
	protected TimeCounter provider;


	public SevenSegmentsDisplayStopWatch(TimeCounter counter)
	{
		super(counter);
		setSegmentsEditable();
		
		this.provider = counter;
		provider.addMilliSecondsInvalidationListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setMilliseconds(((SimpleIntegerProperty) observable).intValue());
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
		provider.addMinutesListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setMinutes(((SimpleIntegerProperty) observable).intValue());
			}
		});
		setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					if (mouseEvent.getClickCount() == 2)
					{
						set(0, 0, 0);
					}
				}
			}
		});		
	}


	@Override
	protected void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		setSegmenet(2, b);
		setSegmenet(3, a);
	}


	@Override
	protected void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		setSegmenet(0, b);
		setSegmenet(1, a);
	}


	private void setMilliseconds(int value)
	{
		int b = (value / 1) % 10;
		int c = (value / 10) % 10;

		setSegmenet(5, b);
		setSegmenet(4, c);
	}


	@Override
	protected void setHours(int value)
	{
		throw new UnsupportedOperationException();
	}


	@Override
	public void set(int minutes, int seconds, int milliSeconds)
	{
		setMinutes(minutes);
		setSeconds(seconds);
		setMilliseconds(milliSeconds);

		provider.set(minutes, seconds, milliSeconds);
	}


	@Override
	public void setCountMode(CounterMode newMode)
	{
		provider.setCountMode(newMode);
	}


	public int getMilliSeconds()
	{
		return getSegment(4).getDisplayedValue() * 100 + getSegment(5).getDisplayedValue() * 10;
	}


	public int getSeconds()
	{
		return getSegment(2).getDisplayedValue() * 10 + getSegment(3).getDisplayedValue();
	}


	public int getMinutes()
	{
		return getSegment(0).getDisplayedValue() * 10 + getSegment(1).getDisplayedValue();
	}


	@Override
	public void startUpate()
	{
		provider.set(getMinutes(), getSeconds(), getMilliSeconds());
		super.startUpate();
	}
}
