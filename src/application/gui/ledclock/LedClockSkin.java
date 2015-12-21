package application.gui.ledclock;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
	private Text text;


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
			if ((idx + 1) % 10 == 1)
			{
				Region line = new Region();
				line.getStyleClass().setAll("line");
				line.setRotate(-idx * 60);
				pane.getChildren().add(line);
				lines.add(line);
			}
			if ((idx + 1) % 5 == 1)
			{
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

		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 40.0);
		text = new Text(getSkinnable().getSkinText());
		text.setFont(font);
		text.setTextOrigin(VPos.TOP);
		text.getStyleClass().add("text");
		text.setMouseTransparent(true);
		text.setText(getSkinnable().getSkinText());

		pane.getChildren().add(text);
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
		getSkinnable().textSkinProperty().addListener(new ChangeListener<String>()
		{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				text.setText(getSkinnable().getSkinText());
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
		size = size - 10;
		pane.setPrefSize(size, size);
		int idx = 3;
		for (Region line : lines)
		{
			double y1 = (size - 0.8 * size) * ((Math.cos(60 * idx * Math.PI / 180)));
			double x1 = (size - 0.8 * size) * ((Math.sin(60 * idx * Math.PI / 180)));
			line.setTranslateX(size / 2 + x1 + (size * 0.026));
			line.setTranslateY(size / 2 + y1 - (size * 0.095));
			line.setPrefSize(0.005 * size, 0.2 * size);
			idx++;
		}

		idx = 30;
		for (Region min : minutes)
		{
			double y1 = (size - 0.55 * size) * ((Math.cos(6 * idx * Math.PI / 180)));
			double x1 = (size - 0.55 * size) * ((Math.sin(6 * idx * Math.PI / 180)));
			min.setTranslateX(size / 2 + x1 + (size * 0.017543859649));
			min.setTranslateY(size / 2 + y1 - (size * 0.004047987616));
			min.setPrefSize(0.025 * size, 0.025 * size);
			idx--;

		}

		idx = hours.size() / 2;
		for (Region h : hours)
		{
			double y1 = (size - 0.3 * size) / 2 * (Math.cos(30 * idx * Math.PI / 180));
			double x1 = (size - 0.3 * size) / 2 * (Math.sin(30 * idx * Math.PI / 180));
			h.setTranslateX(size / 2 + x1 + (size * 0.018575851393));
			h.setTranslateY(size / 2 + y1 - (size * 0.004047987616));
			h.setPrefSize(0.025 * size, 0.025 * size);
			idx--;
		}

		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10.0 / 144.0 * size);
		text.setFont(font);
		
		if (text.getLayoutBounds().getWidth() > 0.78 * size)
		{
			text.setText("...");
		}
		
		text.setTranslateY((size / 2 - (text.getLayoutBounds().getHeight()) * 0.5));
		text.setTranslateX((size / 2 + (text.getLayoutBounds().getWidth() - 15) * 0.5));
	}

}
