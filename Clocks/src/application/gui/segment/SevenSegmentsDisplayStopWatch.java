package application.gui.segment;

import application.counter.Counter;
import application.counter.CounterMode;
import application.counter.TimeCounter;
import application.gui.Updateable;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;

public class SevenSegmentsDisplayStopWatch extends SevenSegmentsDisplay implements Counter, Updateable
{

	public SevenSegmentsDisplayStopWatch(TimeCounter timeProvider)
	{
		super(timeProvider);
		((TimeCounter) provider).addMilliSecondsInvalidationListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setMilliseconds(((SimpleIntegerProperty) observable).intValue());
			}
		});
		((TimeCounter) provider).addSecondsInvalidationListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setSeconds(((SimpleIntegerProperty) observable).intValue());
			}
		});
		((TimeCounter) provider).addMinutesListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				setMinutes(((SimpleIntegerProperty) observable).intValue());
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
		//
	}


	@Override
	public void set(int minutes, int seconds, int milliSeconds)
	{
		setMinutes(minutes);
		setSeconds(seconds);
		setMilliseconds(milliSeconds);

		((TimeCounter) provider).set(minutes, seconds, milliSeconds);
	}


	@Override
	public void setCountMode(CounterMode newMode)
	{
		((TimeCounter) provider).setCountMode(newMode);
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
		((TimeCounter) provider).set(getMinutes(), getSeconds(), getMilliSeconds());
		super.startUpate();
	}
}
