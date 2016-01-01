package application.gui.ledclock;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
	public static String RED = "-led-color: rgb(255, 0, 0);";
	public static String GREEN = "-led-color: rgb(173, 255, 47);";
	public static String SILVER = "-led-color: rgb(240, 240, 240);";


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

		// lines
		for (int idx = 0; idx < 12; idx++)
		{
			Region line = new Region();
			line.getStyleClass().setAll("line");
			line.setRotate(-idx * 30);

			Lighting l = new Lighting();
			Distant light = new Distant();
			light.setAzimuth(-idx * 30);
			l.setSurfaceScale(10.0f);
			l.setLight(light);
			line.setEffect(l);

			pane.getChildren().add(line);
			lines.add(line);
		}

		// minutes
		for (int idx = 0; idx < 60; idx++)
		{
			Region minute = new Region();
			minute.getStyleClass().setAll("on-led");
			pane.getChildren().add(minute);
			minutes.add(minute);
		}

		// hours
		for (int idx = 0; idx < 12; idx++)
		{
			Region hour = new Region();
			hour.getStyleClass().setAll("on-led");
			pane.getChildren().add(hour);
			hours.add(hour);
		}

		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 40.0);
		text = new Text(getSkinnable().getSkinText());
		text.setFont(font);
		text.setTextOrigin(VPos.TOP);
		text.getStyleClass().add("text");
		text.setMouseTransparent(true);
		text.setText(getSkinnable().getSkinText());
		pane.getChildren().add(text);

		getChildren().setAll(pane);
		layout();
	}


	private void handleNewMinutes(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<Integer> newO = (SimpleObjectProperty<Integer>) observable;
		int newValue = newO.get();
		for (int idx = 1; idx < newValue; idx++)
		{
			if (idx % 5 == 0)
			{
				minutes.get(idx).setStyle(GREEN);
			}
			else
			{
				minutes.get(idx).setStyle(RED);
			}
		}
	}


	private void handleNewSeconds(Observable observable)
	{
		Timeline watchLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				minutes.get(0).setStyle(GREEN);
			}
		}), new KeyFrame(Duration.millis(500)));
		watchLine.setOnFinished(new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				minutes.get(0).setStyle(SILVER);
			}
		});
		watchLine.play();
	}


	private void handleNewHours(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<Integer> newO = (SimpleObjectProperty<Integer>) observable;
		int newValue = newO.get();
		for (int idx = 0; idx < newValue; idx++)
		{
			hours.get(idx + 1).setStyle(RED);
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
		getSkinnable().addSecondsChangedListener(new ChangeListener<Integer>()
		{

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue)
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
				layout();
			}
		});
		getSkinnable().heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layout();
			}
		});
		getSkinnable().textSkinProperty().addListener(new ChangeListener<String>()
		{

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				text.setText(getSkinnable().getSkinText());
				layoutText();
			}
		});
	}


	protected void layout()
	{
		double size = getSkinnable().getWidth() < getSkinnable().getHeight() ? getSkinnable().getWidth()
				: getSkinnable().getHeight();

		if (size == 0)
		{
			return;
		}
		size = size - 10; // 10 = insets
		pane.setPrefSize(size, size);

		layoutLines(size);
		layoutLeds(minutes, 0.1, 6, size);
		layoutLeds(hours, 0.3, 30, size);
		layoutText();
	}


	private void layoutLines(double size)
	{
		double y1 = 0d;
		double x1 = 0d;
		double newWidth = 0;
		double newHeigths = 0;

		int idx = 0;
		for (Region line : lines)
		{
			newWidth = Math.round(0.005 * size);
			if (idx == 0 || idx == 3 || idx == 6 || idx == 9 || idx == 12)
			{
				newHeigths = Math.round(0.425 * size);
				y1 = (size - 0.715 * size) * ((Math.cos(30 * idx * Math.PI / 180)));
				x1 = (size - 0.715 * size) * ((Math.sin(30 * idx * Math.PI / 180)));
			}
			else
			{
				newHeigths = Math.round(0.5 * size);
				y1 = (size - 0.675 * size) * ((Math.cos(30 * idx * Math.PI / 180)));
				x1 = (size - 0.675 * size) * ((Math.sin(30 * idx * Math.PI / 180)));
			}
			line.setTranslateX(size * 0.5 + x1 - (newWidth * 0.4));
			line.setTranslateY(size * 0.5 + y1 - (newHeigths * 0.5));
			line.setPrefSize(newWidth, newHeigths);
			idx++;
		}
	}


	private static void layoutLeds(List<Region> nodes, double circleRad, double radStep, double size)
	{
		double x1 = 0;
		double y1 = 0;
		int idx = nodes.size() / 2; // => start layout at 180° (top)
		for (Region h : nodes)
		{
			double newRad = Math.round(0.021 * size);
			y1 = (size - circleRad * size) / 2 * (Math.cos(radStep * idx * Math.PI / 180));
			x1 = (size - circleRad * size) / 2 * (Math.sin(radStep * idx * Math.PI / 180));
			h.setTranslateX(size * 0.5 + x1 - (newRad * 0.5));
			h.setTranslateY(size * 0.5 + y1 - (newRad * 0.5));
			h.setPrefSize(newRad, newRad);
			((DropShadow) h.getEffect()).setRadius(0.02 * size);
			idx--;
		}
	}


	private void layoutText()
	{
		double size = getSkinnable().getWidth() < getSkinnable().getHeight() ? getSkinnable().getWidth()
				: getSkinnable().getHeight();

		size = size - 10; // 10 = insets
		if (size <= 0)
		{
			return;
		}
		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10.0 / 144 * size);
		text.setFont(font);
		double textWidth = text.getLayoutBounds().getWidth();

		if (text.getLayoutBounds().getWidth() > 0.78 * size)
		{
			text.setText("...");
		}
		text.setTranslateY((size * 0.5 - (text.getLayoutBounds().getHeight()) * 0.5));
		text.setTranslateX((size * 0.5 - (textWidth * 0.45)));
	}
}
