package application.gui.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.oopitis.weather.WeatherProperty;
import com.oopitis.weather.owm.OwmWeather;

import application.gui.info.WeatherInfo.WeatherInfoStringBuilder;
import application.gui.util.ShowProgressThread;
import application.gui.webcam.WebImage;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class WeatherPane extends DatePane
{
	private final ProgressIndicator pi = new ProgressIndicator(0);
	private WeatherChart wChart;
	private WebImage image;
	private float[] coordinates = new float[2];


	public WeatherPane(Duration duration, float latitude, float longitude, String imageUrl)
	{
		super(duration);
		image = new WebImage(imageUrl);
		coordinates[0] = latitude;
		coordinates[1] = longitude;
		pi.setId("progress");
		pi.setPrefSize(50, 50);
		pi.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				System.exit(0);
			}
		});
		build();
	}


	private void build()
	{
		int idx = 1;
		fontSize.bind(widthProperty().add(heightProperty()).divide(20));

		HBox pics = new HBox(5);
		pics.setAlignment(Pos.CENTER);
		addRow(idx++, pics);

		VBox weather = new VBox(5);
		weather.setAlignment(Pos.CENTER);
		addRow(idx++, weather);

		addRow(idx++, image);
		GridPane.setFillWidth(image, true);

		VBox chart = new VBox();
		wChart = new WeatherChart();
		chart.getChildren().add(wChart);
		chart.setAlignment(Pos.CENTER);
		addRow(idx++, chart);
		GridPane.setFillWidth(chart, true);

		VBox progress = new VBox();
		progress.setAlignment(Pos.CENTER);
		progress.getChildren().add(pi);
		addRow(idx++, progress);

		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.NEVER);
		getRowConstraints().add(rc);

		rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		getRowConstraints().add(rc);

		rc = new RowConstraints();
		rc.setVgrow(Priority.NEVER);
		getRowConstraints().add(rc);

		rc = new RowConstraints();
		rc.setVgrow(Priority.NEVER);
		getRowConstraints().add(rc);

		rc = new RowConstraints();
		rc.setVgrow(Priority.NEVER);
		getRowConstraints().add(rc);
	}


	private void addWeather(Map<WeatherProperty<?>, Object> infos)
	{
		Pane pictures = (Pane) getChildren().get(1);
		pictures.getChildren().clear();
		Pane weather = (Pane) getChildren().get(2);
		weather.getChildren().clear();

		if (infos.size() == 0)
		{
			wChart.update(-1f, OwmWeather.PressureHectopascal);
			return;
		}

		if (infos.containsKey(WeatherProperties.ActPic))
			pictures.getChildren().add((WebImage) infos.get(WeatherProperties.ActPic));
		if (infos.containsKey(WeatherProperties.ForePic))
			pictures.getChildren().add((WebImage) infos.get(WeatherProperties.ForePic));

		for (Entry<WeatherProperty<?>, Object> entry : infos.entrySet())
		{
			if (entry.getValue() instanceof String)
			{
				weather.getChildren().add(createText("" + entry.getValue()));
			}
			else if (entry.getKey().equals(OwmWeather.PrecipMillimeterPerHour)
					|| entry.getKey().equals(OwmWeather.PressureHectopascal))
			{
				wChart.update(entry.getValue(), entry.getKey());
			}
		}
		layoutChildren();
	}


	private Map<WeatherProperty<?>, Object> getWeather()
	{
		try
		{
			WeatherInfo ws = new WeatherInfoStringBuilder(coordinates[0], coordinates[1]).getTemperature("%1.1f°C")
					.getPressureHectopascal().getHumidityPercent("%1.0f%%").getWindMeterPerSecond("Wind %1.1f m/s")
					.getWindDirectionDegree().getTempNightCelsius("Min %1.0f°C").getTempDayCelsius("Max %1.0f°C")
					.getSunriseTime("H:mm").getSunsetTime("H:mm").getAlertTitel().build();

			return ws.getWeather();
		}
		catch (Exception ex)
		{
			return new HashMap<WeatherProperty<?>, Object>();
		}
	}


	private void bindProgress(ObservableValue<? extends Number> toBind)
	{
		pi.progressProperty().unbind();
		pi.progressProperty().bind(toBind);
	}


	private void startUpate(Duration update)
	{
		ShowProgressThread pt = new ShowProgressThread();
		bindProgress(pt.progressProperty());
		pt.startUpdateThread(update.subtract(Duration.seconds(1d)));
	}


	@Override
	public void update(Duration update)
	{
		super.update(update);
		startUpate(update);
		addWeather(getWeather());
		image.update();
	}

}
