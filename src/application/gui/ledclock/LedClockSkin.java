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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
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

		// lines
		for (int idx = 0; idx < 6; idx++)
		{
			Region line = new Region();
			line.getStyleClass().setAll("line");
			line.setRotate(-idx * 60);
			DropShadow lineDS = new DropShadow(BlurType.TWO_PASS_BOX, Color.SILVER.darker(), 2, 0, 1, 1);
			line.setEffect(lineDS);
			pane.getChildren().add(line);
			lines.add(line);
		}

		// minutes
		for (int idx = 0; idx < 60; idx++)
		{
			Region minute = new Region();
			minute.getStyleClass().setAll("off-led");
			minute.setMouseTransparent(true);

			InnerShadow ledOnInnerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 8, 0, 0, 0);
			DropShadow ledOnGlow = new DropShadow(BlurType.TWO_PASS_BOX,
					getSkinnable().getLedColor().darker().darker().darker(), 20, 0, 0, 0);
			ledOnGlow.setOffsetX(0.0);
			ledOnGlow.setOffsetY(0.0);
			ledOnGlow.setRadius(9.0 / 250.0 * PREFERRED_WIDTH);
			ledOnGlow.setColor(getSkinnable().getLedColor());
			ledOnGlow.setBlurType(BlurType.TWO_PASS_BOX);
			ledOnGlow.setInput(ledOnInnerShadow);
			minute.setEffect(ledOnGlow);
			if ((idx + 1) % 5 == 1)
			{
				minute.setStyle("-led-color: " + colorToCss(Color.RED) + ";");
			}
			pane.getChildren().add(minute);
			minutes.add(minute);

		}

		// hours
		for (int idx = 0; idx < 12; idx++)
		{
			Region hour = new Region();
			hour.getStyleClass().setAll("off-led");

			InnerShadow ledOnInnerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 8, 0, 0, 0);
			DropShadow ledOnGlow = new DropShadow(BlurType.TWO_PASS_BOX,
					getSkinnable().getLedColor().darker().darker().darker(), 20, 0, 0, 0);
			ledOnGlow.setRadius(9.0 / 250.0 * PREFERRED_WIDTH);
			ledOnGlow.setColor(getSkinnable().getLedColor());
			ledOnGlow.setBlurType(BlurType.TWO_PASS_BOX);
			ledOnGlow.setInput(ledOnInnerShadow);
			hour.setEffect(ledOnGlow);

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
		pane.setMouseTransparent(true);

		InnerShadow paneInnerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 3, 0, -5, -5);
		DropShadow paneDropShadow = new DropShadow(BlurType.TWO_PASS_BOX, Color.GRAY.darker().darker().darker(), 20, 0,
				10, 10);
		paneDropShadow.setRadius(10.0 / 250.0 * PREFERRED_WIDTH);
		paneDropShadow.setColor(Color.GRAY.darker());
		paneDropShadow.setBlurType(BlurType.TWO_PASS_BOX);
		paneDropShadow.setInput(paneInnerShadow);
		pane.setEffect(paneDropShadow);

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
			{
				if ((idx + 1) % 5 == 1)
				{
					((DropShadow) minutes.get(idx).getEffect()).setColor(Color.RED);
				}
				else
					((DropShadow) minutes.get(idx).getEffect()).setColor(Color.GREEN);

				minutes.get(idx).getStyleClass().setAll("on-led");

			}
			else
			{
				DropShadow ef = (DropShadow) minutes.get(idx).getEffect();
				ef.setColor(Color.SILVER);
				minutes.get(idx).getStyleClass().setAll("off-led");
			}
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
		{
			((DropShadow) minutes.get(0).getEffect()).setColor(Color.RED);
			minutes.get(0).getStyleClass().setAll("on-led");
		}
		else
		{
			((DropShadow) minutes.get(0).getEffect()).setColor(Color.SILVER);
			minutes.get(0).getStyleClass().setAll("off-led");
		}
	}


	private void handleNewHours(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<BitSet> newO = (SimpleObjectProperty<BitSet>) observable;
		BitSet set = (BitSet) newO.get();
		for (int idx = 0; idx < hours.size(); idx++)
		{
			if (set.get(idx))
			{
				((DropShadow) hours.get(idx).getEffect()).setColor(Color.GREEN);
				hours.get(idx).getStyleClass().setAll("on-led");
			}
			else
			{
				((DropShadow) hours.get(idx).getEffect()).setColor(Color.SILVER);
				hours.get(idx).getStyleClass().setAll("off-led");
			}
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
				resizeText();
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
		size = size - 10; // 10 = insets
		pane.setPrefSize(size, size);

		reszieLines(size);
		resizeLeds(minutes, 0.1, 6, size);
		resizeLeds(hours, 0.3, 30, size);
		resizeText();
	}


	private void reszieLines(double size)
	{
		double y1 = 0d;
		double x1 = 0d;
		double newWidth = 0;
		double newHeigths = 0;

		int idx = 0;
		for (Region line : lines)
		{
			newWidth = Math.round(0.005 * size);
			if (idx == 1 || idx == 2 || idx == 4 || idx == 5)
			{
				newHeigths = Math.round(0.5 * size);
				y1 = (size - 0.675 * size) * ((Math.cos(60 * idx * Math.PI / 180)));
				x1 = (size - 0.675 * size) * ((Math.sin(60 * idx * Math.PI / 180)));
			}
			else
			{
				newHeigths = Math.round(0.425 * size);
				y1 = (size - 0.715 * size) * ((Math.cos(60 * idx * Math.PI / 180)));
				x1 = (size - 0.715 * size) * ((Math.sin(60 * idx * Math.PI / 180)));
			}
			line.setTranslateX(size * 0.5 + x1 - (newWidth * 0.4));
			line.setTranslateY(size * 0.5 + y1 - (newHeigths * 0.5));
			line.setPrefSize(newWidth, newHeigths);
			idx++;
		}
	}


	private static void resizeLeds(List<Region> nodes, double circleRad, double radStep, double size)
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


	private void resizeText()
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
		double textWidth = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(text.getText(),
				text.getFont());

		if (text.getLayoutBounds().getWidth() > 0.78 * size)
		{
			text.setText("...");
		}
		text.setTranslateY((size * 0.5 - (text.getLayoutBounds().getHeight()) * 0.5));
		text.setTranslateX((size * 0.5 - (textWidth * 0.45)));
	}
}
