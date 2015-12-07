package application.gui.ledbutton;

import application.gui.segment.TimeProvider;

public class TimeCounter implements TimeProvider
{
	private final int seconds;
	private final int minutes;
	private final int hours;
	private int actSeconds;
	private int actMinutes;
	private int actHours;


	public TimeCounter(int h, int min, int sec)
	{
		actSeconds = seconds = sec;
		actMinutes = minutes = min;
		actHours = hours = h;
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


	/**
	 * Decrements the counter by 1 second.
	 * 
	 * @return false if the counter was max before decrementing.
	 */
	public synchronized boolean decrement()
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
	public synchronized boolean increment()
	{
		if (actMinutes == 99 && actSeconds == 59 && actHours == 59)
			return false;

		actSeconds += 1;
		if (actSeconds == 60)
		{
			if (actMinutes < 59)
			{
				actMinutes += 1;
				actSeconds = 0;
			}
			else if (actHours < 99)
				actHours += 1;
		}
		return true;
	}


	public void reset()
	{
		actSeconds = seconds;
		actMinutes = minutes;
	}


	public String getText()
	{
		return String.format("%02d:%02d", actMinutes, actSeconds);
	}

}
