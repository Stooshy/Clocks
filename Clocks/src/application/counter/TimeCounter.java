package application.counter;

import application.ObserveableTimeProvider;
import application.gui.CounterMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class TimeCounter extends ObserveableTimeProvider
{
	private int actMilliSeconds;
	private int actSeconds;
	private int actMinutes;
	private CounterMode mode;


	public TimeCounter(Duration update)
	{
		super(update);
		addMilliSecondsChangedListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				actMilliSeconds = 99 - newValue.intValue() / 10;
			}
		});

		addSecondsChangedListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				count();
			}
		});
	}


	@Override
	public int getHours()
	{
		return 0;
	}


	@Override
	public int getMinutes()
	{
		return actMinutes;
	}


	@Override
	public int getSeconds()
	{
		return actSeconds;
	}


	@Override
	public int getMilliSeconds()
	{
		return actMilliSeconds;
	}


	public void count()
	{
		if (mode == CounterMode.UP)
			increment();
		else
			decrement();
	}


	private void decrement()
	{
		if (actSeconds == 0)
		{
			if (actMinutes == 0)
			{
				actMilliSeconds = 0;
				milliSeconds.set(0);
				minutes.set(0);
				seconds.set(0);
				pauseUpdate();
				return;
			}
			else
			{
				actMinutes--;
				minutes.set(actMinutes);
				actSeconds = 60;
			}
		}
		actSeconds--;
		// actMilliSeconds = 99 - getMilliSeconds() / 10;
	}


	private void increment()
	{
		if (actMinutes == 99 && actSeconds == 59)
			pauseUpdate();

		if (actMilliSeconds < getMilliSeconds())
			return;

		actSeconds++;
		if (actSeconds == 60)
		{
			if (actMinutes < 99)
			{
				actSeconds = 0;
				actMinutes++;
			}
			else
			{
				actMilliSeconds = 0;
				actMinutes = 59;
			}
		}
		actMilliSeconds = getMilliSeconds();
		seconds.set(actSeconds);
		minutes.set(actMinutes);
	}


	public void set(int minutes, int seconds, int milliSeconds)
	{
		actSeconds = seconds;
		actMinutes = minutes;
		actMilliSeconds = milliSeconds;
	}


	public void setMode(CounterMode newMode)
	{
		mode = newMode;
	}
}
