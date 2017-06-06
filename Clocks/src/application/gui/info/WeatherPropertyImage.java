package application.gui.info;

import java.util.Locale;

import com.oopitis.weather.WeatherProperty;

import javafx.scene.image.ImageView;

class WeatherPropertyImage extends WeatherProperty<ImageView>
{

	protected WeatherPropertyImage(String constantName)
	{
		super(constantName);
	}


	@Override
	public Class<ImageView> getValueClass()
	{
		return ImageView.class;
	}


	@Override
	public String format(ImageView paramT, Locale paramLocale)
	{
		return "";
	}
}
