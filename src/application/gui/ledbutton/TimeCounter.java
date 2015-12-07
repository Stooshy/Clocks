package application.gui.ledbutton;

import application.gui.CounterMode;
import application.gui.segment.TimeProvider;

public class TimeCounter implements TimeProvider
{
	private int seconds;
	private int minutes;
	private int hours;
	private int actSeconds;
	private int actMinutes;
	private int actHours;

	private CounterMode mode;


	public TimeCounter(int h, int min, int sec)
	{
		mode = CounterMode.DOWN;
		actSeconds = seconds = sec;
		actMinutes = minutes = min;
		actHours = hours = h;
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
	private synchronized boolean decrement()
	{
		if (actSeconds == 0 && actMinutes == 0 && hours == 0)
			return false;

		if (actSeconds == 0)
		{
			if (actMinutes > 0)
			{
				actMinutes -= 1;
				actSeconds = 59;
			}
			else if (actHours > 0)
			{
				actHours -= 1;
				actMinutes = 59;
				actSeconds = 59;
			}
		}
		else
			actSeconds -= 1;

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
	private synchronized boolean increment()
	{
		if (actMinutes == 99 && actSeconds == 59 && actHours == 59)
			return false;

		actSeconds += 1;
		if (actSeconds == 60)
		{
			actSeconds = 59;
			if (actMinutes < 59)
			{
				actMinutes += 1;
				actSeconds = 0;
			}
			else
			{
				if (actHours < 99)
				{
					actHours += 1;
					actMinutes = 0;
					actSeconds = 0;
				}
			}
		}
		return true;
	}


	public void reset()
	{
		actSeconds = seconds;
		actMinutes = minutes;
		actHours = hours;
	}


	public String getText()
	{
		return String.format("%02d:%02d", actMinutes, actSeconds);
	}


	public void setMode(CounterMode newMode)
	{
		mode = newMode;
	}


	public int setSeconds()
	{
		return seconds;
	}

}
