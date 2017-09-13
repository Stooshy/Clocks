package application;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import application.gui.TimeProvider;

public class LocalTimeProvider implements TimeProvider
{
	private static LocalTimeProvider _instance;


	private LocalTimeProvider()
	{

	}


	public static TimeProvider getInstance()
	{
		if (_instance == null)
			_instance = new LocalTimeProvider();
		return _instance;
	}


	@Override
	public int getSeconds()
	{
		return LocalTime.now().getSecondOfMinute();
	}


	@Override
	public int getMinutes()
	{
		return LocalTime.now().getMinuteOfHour();
	}


	@Override
	public int getHours()
	{
		return LocalTime.now(DateTimeZone.forID("Europe/Berlin")).getHourOfDay();
	}


	@Override
	public int getMilliSeconds()
	{
		return LocalTime.now().getMillisOfSecond();
	}

}
