package application.gui.info;

import java.util.Locale;

import com.oopitis.weather.WeatherProperty;

public class WeatherPropertyString extends WeatherProperty<String>
{

	public WeatherPropertyString(String constantName)
	{
		super(constantName);
	}


	public WeatherPropertyString(String constantName, String resourceBundleName, String resourceKey)
	{
		super(constantName, resourceBundleName, resourceKey);
	}


	@Override
	public Class<String> getValueClass()
	{
		return String.class;
	}


	@Override
	public String format(String paramT, Locale paramLocale)
	{
		return "";
	}

}
