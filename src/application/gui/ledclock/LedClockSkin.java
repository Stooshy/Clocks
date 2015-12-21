package application.gui.ledclock;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class LedClockSkin extends SkinBase<LedControl> implements Skin<LedControl>
{
	public static final double MINIMUM_WIDTH = 10;
	public static final double MINIMUM_HEIGHT = 10;
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = 1024;
	public static final double PREFERRED_WIDTH = 250;
	public static final double PREFERRED_HEIGHT = 250;
	private final Pane pane = new Pane();
	private final List<Region> minutes = new ArrayList<Region>();
	private final List<Region> hours = new ArrayList<Region>();
	private final List<Region> lines = new ArrayList<Region>();


	public LedClockSkin(LedControl control)
	{
		super(control);

		pane.setId("ledclockpane");
		init();
		buildPane();
		addListeners();
	}


	private void init()
	{
		if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0
				|| Double.compare(getSkinnable().getWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getHeight(), 0.0) <= 0)
		{
			if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0)
			{
				getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
			}
			else
			{
				getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0)
		{
			getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		}

		if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0
				|| Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0)
		{
			getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHTS);
		}
	}


	private void buildPane()
	{
		for (int idx = 0; idx < 60; idx++)
		{
			Region off = new Region();
			off.getStyleClass().setAll("off-led");
			pane.getChildren().add(off);
			minutes.add(off);
			if ((idx + 1) % 5 == 1)
			{
				Region line = new Region();
				line.getStyleClass().setAll("line");
				line.setPrefSize(1, 80);
				line.setRotate(idx * 30);
				pane.getChildren().add(line);
				lines.add(line);
				off.setStyle("-led-color: " + colorToCss(Color.RED) + ";");
			}
		}

		for (int idx = 0; idx < 12; idx++)
		{
			Region off = new Region();
			off.getStyleClass().setAll("off-led");

			pane.getChildren().add(off);
			hours.add(off);
		}
		pane.setMouseTransparent(true);
		getChildren().setAll(pane);
		resize();
	}


	private void handleNewMinutes(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<BitSet> newO = (SimpleObjectProperty<BitSet>) observable;
		BitSet set = (BitSet) newO.get();
		for (int idx = 1; idx < minutes.size(); idx++)
		{
			if (set.get(idx))
				minutes.get(idx).getStyleClass().setAll("on-led");
			else
				minutes.get(idx).getStyleClass().setAll("off-led");
		}
	}


	public static String colorToCss(final Color COLOR)
	{
		StringBuilder cssColor = new StringBuilder();
		cssColor.append("rgba(").append((int) (COLOR.getRed() * 255)).append(", ")
				.append((int) (COLOR.getGreen() * 255)).append(", ").append((int) (COLOR.getBlue() * 255)).append(", ")
				.append(COLOR.getOpacity()).append(");");
		return cssColor.toString();
	}


	private void handleNewSeconds(Observable observable)
	{
		if (minutes.get(0).getStyleClass().toString().equals("off-led"))
			minutes.get(0).getStyleClass().setAll("on-led");
		else
			minutes.get(0).getStyleClass().setAll("off-led");
	}


	private void handleNewHours(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<BitSet> newO = (SimpleObjectProperty<BitSet>) observable;
		BitSet set = (BitSet) newO.get();
		for (int idx = 0; idx < hours.size(); idx++)
		{
			if (set.get(idx))
				hours.get(idx).getStyleClass().setAll("on-led");
			else
				hours.get(idx).getStyleClass().setAll("off-led");
		}
	}


	private void addListeners()
	{
		getSkinnable().addMinutesListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				handleNewMinutes(observable);
			}
		});
		getSkinnable().addSecondsChangedListener(new ChangeListener<BitSet>()
		{

			@Override
			public void changed(ObservableValue<? extends BitSet> observable, BitSet oldValue, BitSet newValue)
			{
				handleNewSeconds(observable);
			}
		});
		getSkinnable().addHoursListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				handleNewHours(observable);
			}
		});

		getSkinnable().widthProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				resize();
			}
		});

		getSkinnable().heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				resize();
			}
		});
	}


	protected void resize()
	{
		double size = getSkinnable().getWidth() < getSkinnable().getHeight() ? getSkinnable().getWidth()
				: getSkinnable().getHeight();

		if (size == 0)
		{
			return;
		}
		pane.setPrefSize(size + 10, size + 10);
		int idx = lines.size() / 2;
		for (Region line : lines)
		{
			double y1 = (size - 0.75 * size) * ((Math.cos(30 * idx * Math.PI / 180)));
			double x1 = (size - 0.75 * size) * ((Math.sin(30 * idx * Math.PI / 180)));
			line.setTranslateX(size / 2 + x1 + (size * 0.026));
			line.setTranslateY(size / 2 + y1 - (size * 0.095));
			line.setPrefSize(0.003 * size, 0.2 * size);
			idx++;
		}

		idx = minutes.size() / 2;
		for (Region min : minutes)
		{
			double y1 = (size - 0.55 * size) * ((Math.cos(6 * idx * Math.PI / 180)));
			double x1 = (size - 0.55 * size) * ((Math.sin(6 * idx * Math.PI / 180)));
			min.setTranslateX(size / 2 + x1 + (size * 0.017543859649));
			min.setTranslateY(size / 2 + y1 - (size * 0.0040479876161));
			min.setPrefSize(0.02 * size, 0.02 * size);
			idx--;

		}

		idx = hours.size() / 2;
		for (Region h : hours)
		{
			double y1 = (size - 0.2 * size) / 2 * (Math.cos(30 * idx * Math.PI / 180));
			double x1 = (size - 0.2 * size) / 2 * (Math.sin(30 * idx * Math.PI / 180));
			h.setTranslateX(size / 2 + x1 + (size * 0.0185758513931));
			h.setTranslateY(size / 2 + y1 - (size * 0.0040479876161));
			h.setPrefSize(0.02 * size, 0.02 * size);
			idx--;
		}
	}

}
