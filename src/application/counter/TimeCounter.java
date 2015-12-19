package application.counter;

import application.LocalTimeProvider;
import application.gui.CounterMode;
import application.gui.TimeProvider;

public class TimeCounter implements TimeProvider
{
	private int seconds;
	private int minutes;
	private int milliSeconds;
	private static int actMilliSeconds;
	private static int actSeconds;
	private static int actMinutes;
	private CounterMode mode;
	private static int lastSec = 0;


	public TimeCounter(int h, int min, int sec, int milli)
	{
		mode = CounterMode.DOWN;
		actMilliSeconds = milli;
		actSeconds = seconds = sec;
		actMinutes = minutes = min;
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


	/**
	 * Decrements or increments the counter by 1 second if the last call to
	 * count() was > 1s ago.
	 * 
	 * @return false if the counter would reached max/min value.
	 */
	public boolean count()
	{
		if (mode == CounterMode.UP)
			return increment();
		else
			return decrement();
	}


	private boolean decrement()
	{
		if (actSeconds == 0 && actMinutes == 0)
		{
			actMilliSeconds = 0;
			return false;
		}

		if (lastSec < LocalTimeProvider.getInstance().getSeconds())
		{
			if (actSeconds > 0)
			{
				actSeconds--;
			}
			else
			{
				if (actMinutes > 0)
				{
					actMinutes--;
					actSeconds = 59;
				}
			}
		}
		actMilliSeconds = 1000 - LocalTimeProvider.getInstance().getMilliSeconds();
		lastSec = LocalTimeProvider.getInstance().getSeconds();
		return true;
	}


	/**
	 * Increments the counter by 1 second.
	 * 
	 * @return false if the counter was max before incrementing it.
	 *         <p>
	 *         max 99:59:59
	 *         </p>
	 */
	private boolean increment()
	{
		if (actMinutes == 99 && actSeconds == 59)
			return false;

		if (lastSec < LocalTimeProvider.getInstance().getSeconds())
		{
			actSeconds++;
			if (actSeconds == 60)
			{
				actSeconds = 59;
				if (actMinutes < 59)
				{
					actMinutes++;
					actSeconds = 0;
				}
			}
		}
		actMilliSeconds = LocalTimeProvider.getInstance().getMilliSeconds();
		lastSec = LocalTimeProvider.getInstance().getSeconds();
		return true;
	}


	public void set(int minutes, int seconds, int milliSeconds)
	{
		actSeconds = this.seconds = seconds;
		actMinutes = this.minutes = minutes;
		actMilliSeconds = this.milliSeconds = milliSeconds;
	}


	public void reset()
	{
		actSeconds = this.seconds;
		actMinutes = this.minutes;
		actMilliSeconds = this.milliSeconds;
	}


	public void setMode(CounterMode newMode)
	{
		mode = newMode;
	}

}
