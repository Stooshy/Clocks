package application.counter;

import application.ObserveableTimeProvider;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

public class TimeCounter extends ObserveableTimeProvider
{
	interface CountType
	{
		public int getValue(Number newValue, int actValue);
	}
	
	private final SimpleIntegerProperty seconds = new SimpleIntegerProperty();
	private final SimpleIntegerProperty minutes = new SimpleIntegerProperty();
	private final SimpleIntegerProperty milliSeconds = new SimpleIntegerProperty();

	private int actMilliSeconds;
	private int actSeconds;
	private int actMinutes;
	private CounterMode mode;
	private CountType decrement = (l, w) -> {
		return Math.abs(w - l.intValue() / 10);
	};
	private CountType increment = (l, w) -> {
		return Math.abs(w + l.intValue() / 10);
	};
	private CountType countType = increment;


	public TimeCounter(Duration update)
	{
		super(update);
		addMilliSecondsChangedListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				milliSeconds.set(countType.getValue(newValue, actMilliSeconds));
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


	public void count()
	{
		if (mode == CounterMode.INCREMENT)
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
				pauseUpdate();
				return;
			}
			else
			{
				actMinutes--;
				actSeconds = 60;
				minutes.set(actMinutes);
				seconds.set(actSeconds);
			}
			minutes.set(actMinutes);
		}
		actSeconds--;
		seconds.set(actSeconds);
	}


	private void increment()
	{
		if (actMinutes >= 99 && actSeconds == 59)
			pauseUpdate();

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
			minutes.set(actMinutes);
		}
		seconds.set(actSeconds);
	}


	public void set(int minutes, int seconds, int milliSeconds)
	{
		actSeconds = seconds;
		actMinutes = minutes;
		actMilliSeconds = milliSeconds;

		this.milliSeconds.set(milliSeconds);
		this.seconds.set(seconds);
		this.minutes.set(minutes);
	}


	public void setCountMode(CounterMode newMode)
	{
		mode = newMode;
		if (mode == CounterMode.DECREMENT)
			countType = decrement;
		else
			countType = increment;
	}


	public void addMinutesListener(InvalidationListener toAdd)
	{
		minutes.addListener(toAdd);
	}


	public void addSecondsInvalidationListener(InvalidationListener toAdd)
	{
		seconds.addListener(toAdd);
	}


	public void addMilliSecondsInvalidationListener(InvalidationListener toAdd)
	{
		milliSeconds.addListener(toAdd);
	}
	
	public int getMinutes()
	{
		return actMinutes;
	}
	
	public int getSeconds()
	{
		return actSeconds;
	}
}
