package application.gui.info;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.oopitis.weather.WeatherProperty;
import com.oopitis.weather.owm.OwmWeather;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WeatherChart extends StackPane
{
	private Series<Number, Number> pressure = new XYChart.Series<Number, Number>();
	private final Series<Number, Number> rain = new XYChart.Series<Number, Number>();
	public static int MAX_ENTRIES = 24;
	public static float YAxisMax = 1050f;
	public static float YAxisMin = 950f;


	public WeatherChart()
	{
		createRainData();
		buildChart();
		setPrefHeight(280);
	}


	private void buildChart()
	{
		NumberAxis xAxis = new NumberAxis(0, MAX_ENTRIES, 3);
		xAxis.setMinorTickCount(0);
		NumberAxis yAxis = new NumberAxis(YAxisMin, YAxisMax, 50d);
		yAxis.setMinorTickCount(5);

		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);
		lineChart.setAlternativeRowFillVisible(false);
		lineChart.setAlternativeColumnFillVisible(false);
		lineChart.setHorizontalGridLinesVisible(false);
		lineChart.setVerticalGridLinesVisible(false);
		lineChart.getXAxis().setVisible(false);
		lineChart.getYAxis().setVisible(false);
		lineChart.setCreateSymbols(false);
		lineChart.getData().add(pressure);
		lineChart.getData().add(rain);

		getChildren().add(lineChart);
	}


	private void displayLabelForData(Node node, String toDisplay)
	{
		final Text dataText = new Text(toDisplay);
		dataText.setFont(new Font("Arial", 10));

		node.parentProperty().addListener(new ChangeListener<Parent>()
		{
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent)
			{
				if (parent == null)
				{
					return;
				}
				Group parentGroup = (Group) parent;
				parentGroup.getChildren().add(dataText);
			}
		});

		node.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
		{
			@Override
			public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds)
			{
				double minY = bounds.getMinY();
				double minX = bounds.getMinX();
				if (minY <= 0d)
					minY = dataText.prefHeight(-1) * 2.0;
				if (minX <= 0d)
					minX = dataText.prefWidth(-1) / 2;
				if (minX > 120)
					minX = minX - dataText.prefWidth(-1);

				dataText.setLayoutX(Math.round(minX + bounds.getWidth() - dataText.prefWidth(-1)));
				dataText.setLayoutY(Math.round(minY - dataText.prefHeight(-1) * 0.5));
			}
		});
	}


	private void createRainData()
	{
		rain.getData().clear();

		int idx = 0;
		int timeOffset = 3; // hours/value
		while (idx - 1 < MAX_ENTRIES / timeOffset)
		{
			Data<Number, Number> data = new XYChart.Data<Number, Number>(idx * timeOffset, 0f);
			data.setNode(new Region());
			displayLabelForData(data.getNode(), "");
			rain.getData().add(data);
			idx++;
		}
	}


	private void updateRain(ArrayList<Float> values)
	{
		int idx = 0;
		final int timeOffset = 3; // hours/value
		float max = values.stream().filter(i -> i != null).limit(8).max(Float::compare).get() * timeOffset;
		float min = values.stream().filter(i -> i != null).limit(8).min(Float::compare).get() * timeOffset;

		Data<Number, Number> data = rain.getData().get(idx);
		Region node = (Region) data.getNode();
		Group parentGroup = (Group) node.parentProperty().get();
		List<Node> labels = parentGroup.getChildren().stream().filter(item -> (item instanceof Text))
				.collect(Collectors.toList());

		while (idx - 1 < MAX_ENTRIES / timeOffset)
		{
			Float value = 0f;
			if (values.get(idx) != null)
			{
				value = values.get(idx) * timeOffset;
			}
			Float val = YAxisMin + normVal(value, min, max);
			rain.getData().get(idx).setYValue(val);

			Text dataText = (Text) labels.get(idx);
			String toDisplay = value > 0.05f ? String.format("%.1f", value) : "";
			dataText.setText(toDisplay);

			idx++;
		}
	}


	public static float normVal(float val, float min, float max)
	{
		return (100.0f * ((val - min) / (max - min)));
	}


	public static float getVal(float val, float min, float max)
	{
		return (100.0f * ((val - min) / (max - min)));
	}


	private void updatePreassure(float value)
	{
		Data<Number, Number> newEntry = new Data<Number, Number>();
		final int size = pressure.getData().size();
		if (size == MAX_ENTRIES)
		{
			pressure.getData().remove(0);
			pressure.getData().forEach(item -> item.setXValue(item.getXValue().intValue() - 1));
			newEntry.setXValue(MAX_ENTRIES);
		}
		else
		{
			newEntry.setXValue(size + 1);
		}
		newEntry.setYValue(value);
		pressure.getData().add(newEntry);
	}


	@SuppressWarnings("unchecked")
	public <T> void update(Object value, WeatherProperty<T> key)
	{
		if (key == OwmWeather.PrecipMillimeterPerHour)
		{
			updateRain((ArrayList<Float>) value);
		}
		else if (key == OwmWeather.PressureHectopascal)
		{
			updatePreassure((float) value);
		}
		else
			throw new IllegalArgumentException(key.getConstantName());
	}
}
