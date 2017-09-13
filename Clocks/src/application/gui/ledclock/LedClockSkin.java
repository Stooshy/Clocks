package application.gui.ledclock;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
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
		buildPane();
		addListeners();
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
			light.setAzimuth(180);
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
			minute.setCache(true);
			minute.setCacheHint(CacheHint.SPEED);
			pane.getChildren().add(minute);
			minutes.add(minute);
		}

		// hours
		for (int idx = 0; idx < 12; idx++)
		{
			Region hour = new Region();
			hour.getStyleClass().setAll("on-led");
			hour.setCache(true);
			hour.setCacheHint(CacheHint.SPEED);
			pane.getChildren().add(hour);
			hours.add(hour);
		}

		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 40.0);
		text = new Text("00");
		text.setFont(font);
		text.setTextOrigin(VPos.TOP);
		text.getStyleClass().add("text");
		text.setMouseTransparent(true);
		pane.getChildren().add(text);

		getChildren().setAll(pane);
	}


	private void handleNewMinutes(int value)
	{
		minutes.forEach(m -> m.setStyle(SILVER));
		for (int idx = 1; idx < value + 1; idx++)
		{
			if (idx % 5 == 0)
			{
				minutes.get(idx).setStyle(RED);
			}
			else
			{
				minutes.get(idx).setStyle(GREEN);
			}
		}
	}


	private void handleNewHours(int value)
	{
		String colour = RED;
		hours.forEach(hour -> hour.setStyle(SILVER));
		for (int idx = 0; idx < value % 12; idx++)
		{
			hours.get(idx + 1).setStyle(colour);
		}
	}


	private void addListeners()
	{
		getSkinnable().addMinutesListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				handleNewMinutes(((SimpleIntegerProperty) observable).intValue());
			}
		});
		getSkinnable().addHoursListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				handleNewHours(((SimpleIntegerProperty) observable).intValue());
			}
		});
		getSkinnable().addSecondsChangedListener(new InvalidationListener()
		{

			@Override
			public void invalidated(Observable observable)
			{
				String ni = minutes.get(0).getStyle().equals(SILVER) ? RED : SILVER;
				minutes.get(0).setStyle(ni);
				text.setText(String.format("%02d", ((SimpleIntegerProperty) observable).intValue()));
			}
		});
		getSkinnable().widthProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layout(((ReadOnlyDoubleProperty) observable).get());
			}
		});

		getSkinnable().heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layout(((ReadOnlyDoubleProperty) observable).get());
			}
		});
	}


	private void layout(Double observable)
	{
		double size = getSkinnable().getHeight();
		if (size == 0d)
		{
			return;
		}

		layoutLines(size);
		layoutLeds(minutes, 0.1, 6, size);
		layoutLeds(hours, 0.3, 30, size);
		layoutText(size);

		Timeline timeline = new Timeline();
		for (Node node : pane.getChildren())
		{
			timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
					new KeyValue(node.translateXProperty(), Math.random() * LedControl.PREF_SIZE, Interpolator.EASE_IN),
					new KeyValue(node.translateYProperty(), Math.random() * LedControl.PREF_SIZE,
							Interpolator.EASE_IN)),
					new KeyFrame(new Duration(5000),
							new KeyValue(node.translateXProperty(), node.getTranslateX(), Interpolator.EASE_OUT),
							new KeyValue(node.translateYProperty(), node.getTranslateY(), Interpolator.EASE_OUT)));
		}
		timeline.setOnFinished(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				getSkinnable().startUpate();
			}
		});
		timeline.play();
	}


	private void layoutLines(double size)
	{
		double y1 = 0d;
		double x1 = 0d;
		double newWidth = Math.round(0.005 * size);
		double newHeigths = 0;
		double rad = 0;
		int idx = 0;

		for (Region line : lines)
		{
			rad = Math.toRadians(30 * idx);
			if (idx == 0 || idx == 3 || idx == 6 || idx == 9)
			{
				newHeigths = Math.round(0.425 * size);
				y1 = size * 0.285 * ((Math.cos(rad)));
				x1 = size * 0.285 * ((Math.sin(rad)));
			}
			else
			{
				newHeigths = Math.round(0.5 * size);
				y1 = size * 0.325 * ((Math.cos(rad)));
				x1 = size * 0.325 * ((Math.sin(rad)));
			}
			line.setTranslateX(size * 0.5 + x1);
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


	private void layoutText(double size)
	{
		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10.0 / 144 * size);
		text.setFont(font);
		text.setTranslateY((size * 0.5 - (text.getLayoutBounds().getHeight() * 0.5)));
		text.setTranslateX((size * 0.5 - (text.getLayoutBounds().getWidth() * 0.5)));
	}
}
