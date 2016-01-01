package application.gui.ledmatrix;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
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
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = 1024;
	public static final double PREFERRED_WIDTH = 512;
	public static final double PREFERRED_HEIGHT = 128;
	private final Pane ledPane = new Pane();
	private final Region[][] leds = new Region[16][128];
	private static String RED = "-led-color: rgb(255, 0, 0);";
	private Text text;
	private final Timeline timeline;
	private Region cover1;
	private Region cover2;
	private double actTextWidth;
	public static int noRows = 15;
	public static int noCols = 57;
	public static int ledSize = 4;


	public LedMatrixSkin(LedMatrixControl control)
	{
		super(control);

		ledPane.setId("ledmatrixpane");
		init();
		buildPane();
		addListeners();
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
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
		for (int row = 0; row < noRows; row++)
		{
			for (int col = 0; col < noCols; col++)
			{
				Region led = new Region();
				led.getStyleClass().setAll("on-led");
				led.setStyle(RED);
				ledPane.getChildren().add(led);
				leds[row][col] = led;
			}
		}

		text = new Text("test");
		Font font = Font.font("DS-Digital", FontWeight.BOLD, FontPosture.REGULAR, 68.0);
		text.getStyleClass().add("text");
		text.setFont(font);
		text.setTextOrigin(VPos.TOP);

		cover1 = new Region();
		cover1.getStyleClass().add("cover");
		cover1.setMaxSize(214, 60);
		cover2 = new Region();
		cover2.setMaxSize(214, 60);
		cover2.getStyleClass().add("cover");

		getChildren().setAll(ledPane, text, cover1, cover2);

		layout();
	}


	private void handleNewValue(Observable observable)
	{
		@SuppressWarnings("unchecked")
		SimpleObjectProperty<boolean[][]> newO = (SimpleObjectProperty<boolean[][]>) observable;
		boolean[][] newValue = newO.get();

		for (int row = 0; row < 16; row++)
		{
			for (int col = 0; col < 16; col++)
			{
				leds[row][col].setVisible(newValue[row][col]);
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
				handleNewValue(observable);
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
				layoutTimeLine();
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
							timeline.jumpTo(new Duration(2500));
							timeline.pause();
						}
						else
							timeline.play();
					}
				}
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
		layoutLeds(leds, getSkinnable().getWidth(), getSkinnable().getHeight());
		layoutText();
		layoutCovers(getSkinnable().getWidth());
	}


	private static void layoutLeds(Region[][] nodes, double width, double heights)
	{
		double x1 = width * 0.5 - noCols * 2;
		double y1 = heights * 0.5 - noRows * 2 + ledSize;

		for (int row = 0; row < noRows; row++)
		{
			for (int col = 0; col < noCols; col++)
			{
				nodes[row][col].setLayoutX(x1);
				nodes[row][col].setLayoutY(heights - y1);
				nodes[row][col].setPrefSize(ledSize, ledSize);
				x1 += ledSize;
			}

			y1 += ledSize;
			x1 = width * 0.5 - noCols * 2;
		}
	}


	private void layoutText()
	{
		text.setTranslateY(0);
	}


	private void layoutTimeLine()
	{
		double textWidth = text.getLayoutBounds().getWidth();

		if (actTextWidth != textWidth)
		{
			actTextWidth = textWidth;
			KeyValue keyValueX = new KeyValue(text.translateXProperty(),
					+Math.round(getSkinnable().getLayoutX() + textWidth));
			KeyFrame keyFrame = new KeyFrame(Duration.millis(0), keyValueX);
			KeyValue endKeyValue = new KeyValue(text.translateXProperty(), -1.0 * Math.round(textWidth));
			KeyFrame endFrame = new KeyFrame(Duration.seconds(5), endKeyValue);

			timeline.getKeyFrames().setAll(keyFrame, endFrame);
		}
	}


	private void layoutCovers(double width)
	{
		cover1.setTranslateX(getSkinnable().getLayoutX() + noCols * ledSize - 8);
		cover2.setTranslateX(-getSkinnable().getLayoutX() - noCols * ledSize + 8);
	}
}
