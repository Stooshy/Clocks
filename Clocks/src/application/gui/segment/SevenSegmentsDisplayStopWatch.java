package application.gui.segment;

import application.ObserveableTimeProvider;
import application.Timer;
import application.counter.TimeCounter;
import application.gui.CounterMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SevenSegmentsDisplayStopWatch extends SevenSegmentsDisplay implements Timer
{

	public SevenSegmentsDisplayStopWatch(ObserveableTimeProvider provider)
	{
		super(provider);

		provider.addMilliSecondsChangedListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				setMilliseconds(newValue.intValue());
			}
		});
	}


	@Override
	public int getMilliSeconds()
	{
		return getSegment(4).getValueDisplayed() * 100 + getSegment(5).getValueDisplayed() * 10;
	}


	@Override
	public int getSeconds()
	{
		return getSegment(2).getValueDisplayed() * 10 + getSegment(3).getValueDisplayed();
	}


	@Override
	public int getMinutes()
	{
		return getSegment(0).getValueDisplayed() * 10 + getSegment(1).getValueDisplayed();
	}


	@Override
	public int getHours()
	{
		return 0;
	}


	@Override
	protected void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(2).set(SevenDigit.values()[b]);
		getSegment(3).set(SevenDigit.values()[a]);
	}


	@Override
	protected void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(0).set(SevenDigit.values()[b]);
		getSegment(1).set(SevenDigit.values()[a]);
	}


	private void setMilliseconds(int value)
	{
		int b = (value / 10) % 10;
		int c = (value / 100) % 10;

		getSegment(5).set(SevenDigit.values()[b]);
		getSegment(4).set(SevenDigit.values()[c]);
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
	public void toggleMode()
	{
		if (isRunning())
		{
			pauseUpdate();
		}
		else
		{
			set(getMinutes(), getSeconds(), getMilliSeconds());
			startUpate();
		}
	}


	public void setMode(CounterMode newMode)
	{
		((TimeCounter) provider).setMode(newMode);
	}
}
