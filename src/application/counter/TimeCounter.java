package application.counter;

import application.LocalTimeProvider;
import application.gui.CounterMode;
import application.gui.TimeProvider;

public class TimeCounter implements TimeProvider
{
	private static int actMilliSeconds;
	private static int actSeconds;
	private static int actMinutes;
	private static CounterMode mode;

	private static TimeCounter _instance;


	public static TimeCounter getInstance()
	{
		if (_instance == null)
			_instance = new TimeCounter();
		return _instance;
	}


	private TimeCounter()
	{
		reset();
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
	 * count() was > 999ms ago.
	 * 
	 * @return false if the counter would reached max/min value.
	 */
	public static boolean count()
	{
		if (mode == CounterMode.UP)
			return increment();
		else
			return decrement();
	}


	private static boolean decrement()
	{
		if (actSeconds == 0 && actMinutes == 0)
		{
			actMilliSeconds = 0;
			return false;
		}

		if (actMilliSeconds < (999 - LocalTimeProvider.getInstance().getMilliSeconds()))
		{
			actSeconds--;
			if (actSeconds == 0)
			{
				if (actMinutes > 0)
				{
					actMinutes--;
					actSeconds = 59;
				}
			}
		}
		actMilliSeconds = 999 - LocalTimeProvider.getInstance().getMilliSeconds();
		return true;
	}


	private static boolean increment()
	{
		if (actMinutes == 99 && actSeconds == 59)
			return false;

		if (actMilliSeconds > LocalTimeProvider.getInstance().getMilliSeconds())
		{
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
		}
		actMilliSeconds = LocalTimeProvider.getInstance().getMilliSeconds();
		return true;
	}


	public static void set(int minutes, int seconds, int milliSeconds)
	{
		actSeconds = seconds;
		actMinutes = minutes;
		actMilliSeconds = milliSeconds;
	}


	public static void reset()
	{
		actSeconds = 0;
		actMinutes = 0;
		actMilliSeconds = 0;
	}


	public static void setMode(CounterMode newMode)
	{
		mode = newMode;
	}

}
