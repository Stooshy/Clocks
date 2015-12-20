package application;

import java.time.LocalTime;

import application.gui.TimeProvider;

public class LocalTimeProvider implements TimeProvider
{
	private static LocalTimeProvider _instance;


	public static TimeProvider getInstance()
	{
		if (_instance == null)
			_instance = new LocalTimeProvider();
		return _instance;
	}


	@Override
	public int getSeconds()
	{
		return LocalTime.now().getSecond();
	}


	@Override
	public int getMinutes()
	{
		return LocalTime.now().getMinute();
	}


	@Override
	public int getHours()
	{
		return LocalTime.now().getHour();
	}


	@Override
	public int getMilliSeconds()
	{
		return LocalTime.now().getNano() / 1000000;
	}
}
