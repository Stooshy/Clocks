package application;

import application.gui.BaseTimeLine;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.util.Duration;

public class ObserveableTimeProvider // extends LocalTimeProvider
{
	protected final SimpleIntegerProperty seconds = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty minutes = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty hours = new SimpleIntegerProperty();
	protected final SimpleIntegerProperty milliSeconds = new SimpleIntegerProperty();
	protected BaseTimeLine timer;


	public ObserveableTimeProvider()
	{
	}


	public ObserveableTimeProvider(Duration update)
	{
		timer = new BaseTimeLine(update)
		{
			@Override
			public void consume()
			{
				set();
			}
		};
	}


	private void set()
	{
		milliSeconds.set(LocalTimeProvider.getInstance().getMilliSeconds());
		seconds.set(LocalTimeProvider.getInstance().getSeconds());
		minutes.set(LocalTimeProvider.getInstance().getMinutes());
		hours.set(LocalTimeProvider.getInstance().getHours());
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


	public void reset()
	{
		hours.set(00);
		seconds.set(00);
		minutes.set(00);
	}

}
