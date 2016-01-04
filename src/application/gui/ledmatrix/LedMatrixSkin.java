package application.gui.ledmatrix;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LedMatrixSkin extends SkinBase<LedMatrixControl> implements Skin<LedMatrixControl>
{
	public static final double MINIMUM_WIDTH = 10;
	public static final double MINIMUM_HEIGHT = 10;
	public static final double PREFERRED_WIDTH = 300;
	public static final double PREFERRED_HEIGHT = 90;
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = PREFERRED_HEIGHT;
	public static int noRows = 8;
	public static int noCols = 64;
	public static int ledSize = 4;
	private static int colOffSet = 0;
	private final Pane ledPane = new Pane();
	private Text text;
	private Timeline timeline;
	private Region[][] leds = new Region[8][noCols];
	private boolean[][] value = new boolean[8][noCols];


	public LedMatrixSkin(LedMatrixControl control)
	{
		super(control);

		ledPane.setId("ledmatrixpane");
		init();
		buildPane();
		layout();
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


	private void buildPane()
	{
		ledPane.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);

		text = new Text();
		Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 56.0);
		text.getStyleClass().add("text");
		text.setFont(font);
		text.setTextOrigin(VPos.TOP);

		for (int row = 0; row < noRows; row++)
		{
			for (int col = 0; col < noCols; col++)
			{
				leds[row][col] = createLed();
			}
		}

		getChildren().setAll(ledPane, text);
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
		if (timeline.getStatus() == Status.STOPPED)
			showValue(0);
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
		getSkinnable().addTextListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable observable)
			{
				text.setText(getSkinnable().getSkinText());
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
							showValue(0);
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
		double size = getSkinnable().getWidth() < getSkinnable().getHeight() ? getSkinnable().getWidth()
				: getSkinnable().getHeight();

		if (size == 0)
		{
			return;
		}
		layoutLeds(getSkinnable().getWidth(), getSkinnable().getHeight());
	}


	private void layoutLeds(double width, double heights)
	{
		ledPane.getChildren().clear();
		noCols = (int) Math.round(width / 4);
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
		int colOffset = 0;
		for (int row = 0; row < value.length; row++)
		{
			for (int col = 0; col < value[0].length; col++)
			{
				if (col + offset < leds[0].length)
					leds[row][col + offset].setVisible(!value[row][col]);
				else if (col < leds[0].length)
				{
					leds[row][colOffset % value[0].length].setVisible(!value[row][colOffset % value[0].length]);
					colOffset++;
				}
			}
		}
	}
}
