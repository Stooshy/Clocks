package application.gui.info;

import com.oopitis.weather.WeatherProperty;
import com.oopitis.weather.owm.OwmWeather;

import javafx.scene.image.ImageView;

public class WeatherProperties extends OwmWeather
{
	public static final WeatherProperty<ImageView> ActPic = new WeatherPropertyImage("ActPic");
	public static final WeatherProperty<ImageView> ForePic = new WeatherPropertyImage("ForePic");
	public static final WeatherProperty<String> City = new WeatherPropertyString("City");
}
