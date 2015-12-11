package application.counter;

import application.LocalTimeProvider;
import application.gui.CounterMode;
import application.gui.TimeProvider;

public class TimeCounter implements TimeProvider
{
	private int seconds;
	private int minutes;
	private int hours;
	private static int actMilliSeconds;
	private static int actSeconds;
	private static int actMinutes;
	private static int actHours;
	private CounterMode mode;
	private static int lastSec = 0;


	public TimeCounter(int h, int min, int sec, int milli)
	{
		mode = CounterMode.DOWN;
		actMilliSeconds = milli;
		actSeconds = seconds = sec;
		actMinutes = minutes = min;
		actHours = hours = h;
	}


	public void setSMilliSeconds(int value)
	{
		actMilliSeconds = value;
	}


	public void setSeconds(int value)
	{
		seconds = value;
	}


	public void setMinutes(int value)
	{
		minutes = value;
	}


	public void setHours(int value)
	{
		hours = value;
	}


	public int getHours()
	{
		return actHours;
	}


	public int getMinutes()
	{
		return actMinutes;
	}


	public int getSeconds()
	{
		return actSeconds;
	}


	public int getMilliSeconds()
	{
		return (int) actMilliSeconds;
	}


	public boolean count()
	{
		if (mode == CounterMode.UP)
			return increment();
		else
			return decrement();
	}


	/**
	 * Decrements the counter by 1 second.
	 * 
	 * @return false if the counter was max before decrementing.
	 */
	private boolean decrement()
	{
		if (actSeconds == 0 && actMinutes == 0 && hours == 0)
		{
			actMilliSeconds = 0;
			return false;
		}

		if (lastSec < LocalTimeProvider.getInstance().getSeconds())
		{
			if (actSeconds > 0)
				actSeconds--;
			else
			{
				if (actMinutes > 0)
				{
					actMinutes--;
					actSeconds = 59;
				}
				else if (actHours > 0)
				{
					actHours--;
					actMinutes = 59;
					actSeconds = 59;
				}
			}
			actMilliSeconds = 1000 - LocalTimeProvider.getInstance().getMilliseconds();
		}
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
		if (actMinutes == 99 && actSeconds == 59 && actHours == 59)
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
				else
				{
					if (actHours < 99)
					{
						actHours++;
						actMinutes = 0;
						actSeconds = 0;
					}
				}
			}
			actMilliSeconds = LocalTimeProvider.getInstance().getMilliseconds();
		}
		lastSec = LocalTimeProvider.getInstance().getSeconds();
		return true;
	}


	public void set()
	{
		actSeconds = seconds;
		actMinutes = minutes;
		actHours = hours;
		actMilliSeconds = 0;
	}


	public void setMode(CounterMode newMode)
	{
		mode = newMode;
	}


	@Override
	public int getMilliseconds()
	{
		return actMilliSeconds;
	}

}
