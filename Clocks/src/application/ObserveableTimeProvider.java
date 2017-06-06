package application;

import application.gui.TimeEngine;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.util.Duration;

public class ObserveableTimeProvider extends LocalTimeProvider
{
	protected final SimpleIntegerProperty seconds = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty minutes = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty hours = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty milliSeconds = new SimpleIntegerProperty();
	private TimeEngine timer;


	public ObserveableTimeProvider(Duration update)
	{
		timer = new TimeEngine(update)
		{
			@Override
			public void consume()
			{
				set();
			}
		};
	}


	public void addMinutesListener(InvalidationListener toAdd)
	{
		minutes.addListener(toAdd);
	}


	public void addSecondsInvalidationListener(InvalidationListener toAdd)
	{
		seconds.addListener(toAdd);
	}


	public void addSecondsChangedListener(ChangeListener<Number> toAdd)
	{
		seconds.addListener(toAdd);
	}


	public void addMilliSecondsChangedListener(ChangeListener<Number> toAdd)
	{
		milliSeconds.addListener(toAdd);
	}


	public void addHoursListener(InvalidationListener toAdd)
	{
		hours.addListener(toAdd);
	}


	private void set()
	{
		milliSeconds.set(getMilliSeconds());
		seconds.set(getSeconds());
		minutes.set(getMinutes());
		hours.set(getHours());
	}


	public void startUpate()
	{
		timer.startUpate();
	}


	public void pauseUpdate()
	{
		timer.pauseUpdate();
	}


	public boolean isRunning()
	{
		return timer.isRunning();
	}

}
