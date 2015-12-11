package application;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import application.counter.TimeCounter;
import application.gui.CounterMode;
import application.gui.ScreensController;
import application.gui.TimeProvider;
import application.gui.ledbutton.LedButton;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.TimeConsumer;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SegmentDemo extends Application
{
	private TimeConsumer stopWatchDisplay;
	private TimeCounter counter;
	private LedButton showCounter;
	private Timeline counterLine;
	private LedButton go;
	private TimeConsumer watchDisplay;
	public static final String WATCH_SCREEN = "watch";
	public static final String COUNTER_SCREEN = "counter";
	private ScreensController mainContainer;


	@Override
	public void init()
	{
		counter = new TimeCounter(0, 0, 10, 0);
		stopWatchDisplay = new SevenSegmentsDisplay();
		stopWatchDisplay.setTimeProvider(counter);

		watchDisplay = new SevenSegmentsDisplay();
		watchDisplay.setTimeProvider(LocalTimeProvider.getInstance());

		mainContainer = new ScreensController();
		mainContainer.addScreen(WATCH_SCREEN, watchDisplay.getPane());
		mainContainer.addScreen(COUNTER_SCREEN, stopWatchDisplay.getPane());
	}


	@Override
	public void start(Stage stage)
	{
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image("file:///" + new File("").getAbsolutePath().replace('\\', '/') + "/logo.png"));
		stage.setTitle("7 Segments");

		final BorderPane borderPane = new BorderPane();
		borderPane.setId("ROOTNODE");
		borderPane.setTop(new Buttons());

		mainContainer.setScreen(WATCH_SCREEN);
		borderPane.setCenter(mainContainer);
		Scene scene = new Scene(borderPane, 200, 100);
		scene.getStylesheets().add(getClass().getResource("7segmentdemo.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);

		addMouseListeners(stage, mainContainer);
		addMouseListeners(stage, borderPane);

		// ********* timelines for time and counter********
		final Timeline watchLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				watchDisplay.consumeTime();
			}
		}), new KeyFrame(Duration.millis(100)));
		watchLine.setCycleCount(Animation.INDEFINITE);
		watchLine.play();

		counterLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				if (counter.count())
					stopWatchDisplay.consumeTime();
				else
				{
					try
					{
						SegmentDemo.this.counterLine.stop();

						Media media = new Media(
								"file:///" + new File("").getAbsolutePath().replace('\\', '/') + "/bell.mp3");
						MediaPlayer player = new MediaPlayer(media);
						player.play();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}), new KeyFrame(Duration.millis(100)));
		counterLine.setCycleCount(Animation.INDEFINITE);
		stage.show();
	}


	private void addMouseListeners(Stage stage, Node node)
	{
		final Delta dragDelta = new Delta();
		node.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				stage.setX(event.getScreenX() + dragDelta.x);
				stage.setY(event.getScreenY() + dragDelta.y);
			}
		});
		node.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				// record a delta distance for the drag and drop operation.
				dragDelta.x = stage.getX() - mouseEvent.getScreenX();
				dragDelta.y = stage.getY() - mouseEvent.getScreenY();
			}
		});
		node.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
				{
					if (mouseEvent.getClickCount() == 2)
					{
						if (showCounter.isSelected())
						{
							if (counterLine.getStatus() == Status.STOPPED)
							{
								go.setSelected(false);
								counter.set();
								stopWatchDisplay.setTime(0, 0, 0, 0);
								stopWatchDisplay.setTimeProvider((TimeProvider) stopWatchDisplay);
							}
						}
					}
				}
			}
		});
	}


	public static void main(String[] args)
	{
		Application.launch(args);
	}

	class Buttons extends GridPane
	{

		public Buttons()
		{
			setId("TOPPANE");
			showCounter = new LedButton("\nShow daytime or counter.");
			showCounter.setSkinText("M");
			showCounter.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue)
					{
						if ((counterLine.getStatus() == Status.STOPPED))
						{
							stopWatchDisplay.consumeTime();
						}
						mainContainer.setScreen(COUNTER_SCREEN);
					}
					else
					{
						mainContainer.setScreen(WATCH_SCREEN);
					}
				}
			});
			add(showCounter, 0, 0);
			GridPane.setValignment(showCounter, VPos.CENTER);
			GridPane.setHalignment(showCounter, HPos.CENTER);

			go = new LedButton();
			go.setSkinText("Go");
			go.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (counterLine.getStatus() == Status.STOPPED)
					{
						counter.setSeconds(((TimeProvider) stopWatchDisplay).getSeconds());
						counter.setMinutes(((TimeProvider) stopWatchDisplay).getMinutes());
						counter.setHours(((TimeProvider) stopWatchDisplay).getHours());
						counter.set();
						stopWatchDisplay.setTimeProvider((TimeProvider) counter);
						SegmentDemo.this.counterLine.play();
					}
					else if (counterLine.getStatus() == Status.RUNNING)
					{
						SegmentDemo.this.counterLine.pause();
					}
					else if (counterLine.getStatus() == Status.PAUSED)
					{
						SegmentDemo.this.counterLine.play();
					}

					if (!newValue)
						SegmentDemo.this.counterLine.stop();
				}
			});

			add(go, 1, 0);
			GridPane.setValignment(go, VPos.CENTER);
			GridPane.setHalignment(go, HPos.CENTER);

			LedButton counterMode = new LedButton();
			counterMode.setSkinText("<");
			counterMode.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue)
					{
						counterMode.setSkinText(">");
						counter.setMode(CounterMode.UP);
					}
					else
					{
						counterMode.setSkinText("<");
						counter.setMode(CounterMode.DOWN);
					}
				}
			});

			add(counterMode, 2, 0);
			GridPane.setValignment(counterMode, VPos.CENTER);
			GridPane.setHalignment(counterMode, HPos.CENTER);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE\nd.M. (w)");
			String dateTxt = LocalDate.now().format(formatter);

			Font font = Font.font("Tahoma", FontWeight.THIN, FontPosture.ITALIC, 12.0);
			final Text text = new Text(dateTxt);
			text.setFont(font);
			text.setTextAlignment(TextAlignment.CENTER);
			text.setWrappingWidth(75);
			add(text, 3, 0);

			// ************ timeline date field****************
			final LastDay today = new LastDay();
			today.day = LocalDate.now().getDayOfMonth();
			final Timeline dateLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent actionEvent)
				{
					if (today.day == LocalDate.now().getDayOfMonth())
						return;

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE\nd.M. (w)");
					text.setText(LocalDate.now().format(formatter));
					today.day = LocalDate.now().getDayOfMonth();
				}
			}), new KeyFrame(Duration.hours(1)));
			dateLine.setCycleCount(Animation.INDEFINITE);
			dateLine.play();

			LedButton closeBtn = new LedButton();
			closeBtn.setSkinText("X");
			closeBtn.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					Platform.exit();
				}
			});
			add(closeBtn, 5, 0);
			GridPane.setValignment(closeBtn, VPos.CENTER);
			GridPane.setHalignment(closeBtn, HPos.CENTER);

			ColumnConstraints col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.LEFT);
			col.setFillWidth(true);
			col.setPrefWidth(75);
			col.setHgrow(Priority.ALWAYS);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

		}
	}

	private class Delta
	{
		double x, y;
	}

	private class LastDay
	{
		int day;
	}

}
