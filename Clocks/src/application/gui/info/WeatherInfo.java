package application.gui.info;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.oopitis.weather.GeoLocation;
import com.oopitis.weather.WeatherProperty;
import com.oopitis.weather.WeatherReport;
import com.oopitis.weather.owm.OwmSummary;
import com.oopitis.weather.owm.OwmWeather;

import application.gui.webcam.WebImage;

class WeatherInfo
{
	private LinkedHashMap<WeatherProperty<?>, Object> weatherMap = new LinkedHashMap<WeatherProperty<?>, Object>();


	private WeatherInfo(WeatherInfoStringBuilder builder)
	{
		weatherMap = builder.items;
	}


	public LinkedHashMap<WeatherProperty<?>, Object> getWeather()
	{
		return weatherMap;
	}

	public static class WeatherInfoStringBuilder
	{
		LinkedHashMap<WeatherProperty<?>, Object> pictures = new LinkedHashMap<WeatherProperty<?>, Object>();
		List<Float> rain = new ArrayList<>();
		LinkedHashMap<WeatherProperty<?>, Object> items = new LinkedHashMap<WeatherProperty<?>, Object>();
		@SuppressWarnings("rawtypes")
		private Map<WeatherProperty, Object> wps;


		public WeatherInfoStringBuilder(float latitude, float longitude)
		{
			getWeather(latitude, longitude);
		}


		public WeatherInfoStringBuilder getTempNightCelsius(String format)
		{
			Optional<Float> t = get(OwmWeather.TempNightCelsius);
			mapAndAdd(t, formatter(format), OwmWeather.TempNightCelsius);
			return this;
		}


		public WeatherInfoStringBuilder getTemperature(String format)
		{
			Optional<Float> t = get(OwmWeather.TempCelsius);
			mapAndAdd(t, formatter(format), OwmWeather.TempCelsius);
			return this;
		}


		public WeatherInfoStringBuilder getWindMeterPerSecond(String format)
		{
			Optional<Float> t = get(OwmWeather.WindMeterPerSecond);
			mapAndAdd(t, formatter(format), OwmWeather.WindMeterPerSecond);
			return this;
		}


		public WeatherInfoStringBuilder getWindDirectionDegree()
		{

			Optional<Float> t = get(OwmWeather.WindDirectionDegree);
			t.map(item -> OwmWeather.WindDirectionDegree.format(item))
					.ifPresent(item -> items.put(OwmWeather.WindDirectionDegree, item));
			return this;
		}


		public WeatherInfoStringBuilder getHumidityPercent(String format)
		{
			Optional<Float> t = get(OwmWeather.HumidityPercent);
			mapAndAdd(t, formatter(format), OwmWeather.HumidityPercent);
			return this;
		}


		public WeatherInfoStringBuilder getPressureHectopascal()
		{
			Optional<Float> t = get(OwmWeather.PressureHectopascal);
			t.ifPresent(item -> items.put(OwmWeather.PressureHectopascal, item));
			return this;
		}


		public WeatherInfoStringBuilder getSunriseTime(String format)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Optional<Date> t = get(OwmWeather.SunriseTime);
			t.map(formatter::format).map(item -> "Tag " + item)
					.ifPresent(item -> items.put(OwmWeather.SunriseTime, item));
			return this;
		}


		public WeatherInfoStringBuilder getSunsetTime(String format)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Optional<Date> t = get(OwmWeather.SunsetTime);
			t.map(formatter::format).map(item -> "Nacht " + item)
					.ifPresent(item -> items.put(OwmWeather.SunsetTime, item));
			return this;
		}


		public WeatherInfoStringBuilder getTempDayCelsius(String format)
		{
			Optional<Float> t = get(OwmWeather.TempDayCelsius);
			mapAndAdd(t, formatter(format), OwmWeather.TempDayCelsius);
			return this;
		}


		public WeatherInfoStringBuilder getCity()
		{
			Optional<Object> t = Optional.of(items.get(WeatherProperties.City));
			mapAndAdd(t, formatter(""), WeatherProperties.City);
			return this;
		}


		public Optional<Float> getRain()
		{
			return get(OwmWeather.PrecipAccumCentimeter);
		}


		public WeatherInfoStringBuilder getAlertTitel()
		{
			Optional<String> t = get(OwmWeather.AlertTitle);
			t.ifPresent(item -> items.put(OwmWeather.AlertTitle, item));
			return this;
		}


		private <T> void mapAndAdd(Optional<T> t, Function<Object, String> mapper, WeatherProperty<?> key)
		{
			t.map(mapper).ifPresent(item -> items.put(key, item));
		}


		@SuppressWarnings("unchecked")
		private <T> Optional<T> get(WeatherProperty<T> value)
		{
			return Optional.ofNullable((T) wps.get(value));
		}


		public WeatherInfo build()
		{
			return new WeatherInfo(this);
		}


		private Optional<WebImage> getImage(WeatherReport report)
		{
			Optional<OwmSummary> os = Optional.ofNullable((OwmSummary) report.get(OwmWeather.WeatherSummary));
			Optional<WebImage> iv = os.map(item -> new WebImage(item.items().get(0).getIconUrl()));
			return iv;
		}


		private Function<Object, String> formatter(String format)
		{
			return (value) -> {
				return String.format(format, value);
			};
		};


		@SuppressWarnings("rawtypes")
		private void getWeather(float latitude, float longitude)
		{
			items.clear();
			try
			{
				OwmWeather om = new OwmWeather("8671014c0553fd14d37516c6269815c2");
				// weather tomorrow
				List<WeatherReport> reports = om.query(new GeoLocation(latitude, longitude), OwmWeather.CONDITIONS_DAY);
				WeatherReport report = reports.get(1);
				items.put(WeatherProperties.City, report.getLocation().getCity());
				getImage(report).ifPresent(item -> items.put(WeatherProperties.ForePic, item));

				// weather forecast today
				report = reports.get(0);
				wps = report.get(OwmWeather.DailyTemperature);
				// actual weather
				reports = om.query(new GeoLocation(latitude, longitude), OwmWeather.CONDITIONS);
				report = reports.get(0);
				getImage(report).ifPresent(item -> items.put(WeatherProperties.ActPic, item));
				wps.putAll(report.get(OwmWeather.Temperature));
				wps.putAll(report.get(OwmWeather.Wind));
				wps.putAll(report.get(OwmWeather.Alert));
				wps.put(OwmWeather.PressureHectopascal, report.get(OwmWeather.PressureHectopascal));
				wps.put(OwmWeather.WeatherSummary, report.get(OwmWeather.WeatherSummary));
				wps.putAll(report.get(OwmWeather.Astronomy));

				// next hours
				reports = om.query(new GeoLocation(latitude, longitude), OwmWeather.CONDITIONS_HOUR);
				report = reports.get(0);
				items.put(OwmWeather.PrecipMillimeterPerHour,
						reports.stream().map(item -> item.get(OwmWeather.Precipitation))
								.map(item1 -> item1.get(OwmWeather.PrecipMillimeterPerHour))
								.collect(Collectors.toList()));
			}
			catch (Exception ex)
			{
				wps = new HashMap<WeatherProperty, Object>();
			}
		}
	}
}
