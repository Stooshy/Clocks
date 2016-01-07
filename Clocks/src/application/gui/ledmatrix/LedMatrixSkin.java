package application.gui.ledmatrix;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class LedMatrixSkin extends SkinBase<LedMatrixControl> implements Skin<LedMatrixControl>
{
	public static final double MINIMUM_WIDTH = 10;
	public static final double MINIMUM_HEIGHT = 10;
	public static final double PREFERRED_WIDTH = 256;
	public static final double PREFERRED_HEIGHT = 90;
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = PREFERRED_HEIGHT;
	public static int noRows = 8;
	public static int noCols = 64;
	public static int ledSize = 4;
	private static int colOffSet = 0;
	private final Pane ledPane = new Pane();
	private Timeline timeline;
	private Region[][] leds = new Region[8][noCols];
	private boolean[][] value = new boolean[8][noCols];
	private boolean needsLayout = true;


	public LedMatrixSkin(LedMatrixControl control)
	{
		super(control);
		
		ledPane.setId("ledmatrixpane");
		getChildren().setAll(ledPane);
		init();
		setTimeLine();
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


	protected static Region createLed()
	{
		Region led = new Region();
		led.setVisible(true);
		led.getStyleClass().setAll("on-led");
		led.setCache(true);
		led.setCacheHint(CacheHint.SPEED);
		led.setPrefSize(ledSize, ledSize);
		return led;
	}


	@SuppressWarnings("unchecked")
	private void handleNewValue(Observable observable)
	{
		value = (boolean[][]) ((ObjectPropertyBase<boolean[][]>) observable).get();
		if (!needsLayout && timeline.getStatus() != Status.RUNNING)
			showValue(colOffSet);
	}


	private void addListeners()
	{
		getSkinnable().heightProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				layout();
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
		getSkinnable().addClickedListener(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if (event.getButton().equals(MouseButton.PRIMARY))
				{
					if (event.getClickCount() == 2)
					{
						if (timeline.getStatus() == Status.RUNNING)
						{
							timeline.stop();
							colOffSet = 0;
							clearDisplay();
						}
						else
							timeline.play();
					}
				}
			}
		});
		getSkinnable().addMinutesListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				handleNewValue(observable);
			}
		});
	}


	private void layout()
	{
		layoutLeds(getSkinnable().getWidth(), getSkinnable().getHeight());
	}


	private void layoutLeds(double width, double heights)
	{
		ledPane.getChildren().clear();
		noCols = (int) Math.round(width / 4) < 64 ? 64 : (int) Math.round(width / 4);
		leds = new Region[noRows][noCols];

		double x1 = width * 0.5 - noCols * 2;
		double y1 = heights * 0.5 - noRows * 2 + ledSize;
		for (int row = 0; row < noRows; row++)
		{
			for (int col = 0; col < noCols; col++)
			{
				leds[row][col] = createLed();
				leds[row][col].setLayoutX(x1);
				leds[row][col].setLayoutY(heights - y1);
				ledPane.getChildren().add(leds[row][col]);
				x1 += ledSize;
			}
			y1 += ledSize;
			x1 = width * 0.5 - noCols * 2;
		}
		needsLayout = false;
	}


	private void setTimeLine()
	{
		timeline = new Timeline(new KeyFrame(Duration.seconds(0)),
				new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent actionEvent)
					{
						showValue(colOffSet);
						colOffSet++;
						if (colOffSet >= noCols)
							colOffSet = 0;
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);
	}


	private void showValue(int offset)
	{
		int ledoffset = 0;
		for (int row = 0; row < noRows; row++)
		{
			ledoffset = offset;
			for (int col = 0; col < value[0].length; col++)
			{
				if (checkRange(ledoffset))
					leds[row][ledoffset].setVisible(!value[row][col]);
				else
					leds[row][ledoffset % noCols].setVisible(!value[row][col]);

				ledoffset++;
			}
		}
	}


	public void clearDisplay()
	{
		for (int row = 0; row < noRows; row++)
		{
			for (int col = 0; col < noCols; col++)
			{
				leds[row][col].setVisible(true);
			}
		}
	}


	private static boolean checkRange(int offset)
	{
		return offset >= 0 && offset < noCols;
	}
}
