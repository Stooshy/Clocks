package application;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.counter.TimeCounter;
import application.gui.CounterMode;
import application.gui.ScreensController;
import application.gui.TimeProvider;
import application.gui.ledbutton.LedButton;
import application.gui.ledclock.LedControl;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.SevenSegmentsDisplayStopWatch;
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
	private LedButton showCounter;
	private Timeline counterLine;
	private List<Timeline> watchLines;
	private LedButton go;
	private TimeConsumer watchDisplay1;
	private TimeConsumer watchDisplay2;
	public static final int WATCH_SCREEN = 1;
	public static final int WATCH_SCREEN2 = 2;
	public static final int COUNTER_SCREEN = 3;
	private ScreensController mainContainer;
	private Stage stage;


	@Override
	public void init()
	{
		watchLines = new ArrayList<Timeline>();
		stopWatchDisplay = new SevenSegmentsDisplayStopWatch();
		watchDisplay1 = new SevenSegmentsDisplay(LocalTimeProvider.getInstance());
		watchDisplay2 = new LedControl(LocalTimeProvider.getInstance());

		mainContainer = new ScreensController();
		mainContainer.addScreen(WATCH_SCREEN2, ((LedControl) watchDisplay2));
		mainContainer.addScreen(WATCH_SCREEN, ((SevenSegmentsDisplay) watchDisplay1).getPane());
		mainContainer.addScreen(COUNTER_SCREEN, ((SevenSegmentsDisplay) stopWatchDisplay).getPane());
	}


	@Override
	public void start(Stage stage)
	{
		this.stage = stage;
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image("file:///" + new File("").getAbsolutePath().replace('\\', '/') + "/logo.png"));
		stage.setTitle("7 Segments");

		final BorderPane borderPane = new BorderPane();
		borderPane.setId("ROOTNODE");
		borderPane.setTop(new Buttons());
		borderPane.setCenter(mainContainer);
		Scene scene = new Scene(borderPane, 200, 260);
		scene.getStylesheets().add(getClass().getResource("7segmentdemo.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setMaxHeight(945);
		stage.setMaxWidth(900);

		addMouseListeners(stage, ((LedControl) watchDisplay2), borderPane);

		// ********* timelines for time and counter********
		final Timeline watchLine1 = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				watchDisplay1.consumeTime();
			}
		}), new KeyFrame(Duration.millis(100)));

		watchLine1.setCycleCount(Animation.INDEFINITE);
		watchLines.add(watchLine1);

		// ********* timelines for time and counter********
		final Timeline watchLine2 = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				watchDisplay2.consumeTime();
			}
		}), new KeyFrame(Duration.millis(100)));
		watchLine2.setCycleCount(Animation.INDEFINITE);
		watchLines.add(watchLine2);

		counterLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				if (!TimeCounter.count())
				{
					try
					{
						go.setSelected(false);
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
				stopWatchDisplay.consumeTime();
			}
		}), new KeyFrame(Duration.millis(20)));
		counterLine.setCycleCount(Animation.INDEFINITE);

		mainContainer.setScreen(WATCH_SCREEN);
		initScreen();
		stage.show();

	}


	private void setCurrentWidthToStage(double number2)
	{
		stage.setWidth(number2);
	}


	private void setCurrentHeightToStage(double number2)
	{
		stage.setHeight(number2);
	}


	private void addMouseListeners(final Stage stage, Node... nodes)
	{
		for (Node nodeToAdd : nodes)
		{
			final Delta dragDelta = new Delta();
			nodeToAdd.setOnMouseDragged(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					stage.setX(event.getScreenX() + dragDelta.x);
					stage.setY(event.getScreenY() + dragDelta.y);
				}
			});
			nodeToAdd.setOnMousePressed(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					// record a delta distance for the drag and drop operation.
					dragDelta.x = stage.getX() - mouseEvent.getScreenX();
					dragDelta.y = stage.getY() - mouseEvent.getScreenY();
				}
			});
			nodeToAdd.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
					{
						if (mouseEvent.getClickCount() == 2)
						{
							if (mainContainer.getActScreen().equals(COUNTER_SCREEN))
							{
								if (counterLine.getStatus() == Status.STOPPED)
								{
									TimeCounter.reset();
									stopWatchDisplay.consumeTime();
								}
							}
						}
					}
				}
			});
		}
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
			showCounter = new LedButton("\nSwitch between displays.");
			showCounter.setSkinText("M");

			showCounter.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					mainContainer.setNextScreen();
					initScreen();
				}
			});

			add(showCounter, 0, 0);
			GridPane.setValignment(showCounter, VPos.CENTER);
			GridPane.setHalignment(showCounter, HPos.CENTER);

			go = new LedButton(
					"\nStart or stop counter.\nIf the counter is stopped douple click on display will reset to 0.\nClick on digit to change value");
			go.setSkinText("Go");
			go.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (counterLine.getStatus() == Status.STOPPED)
					{
						TimeCounter.set(((TimeProvider) stopWatchDisplay).getMinutes(),
								((TimeProvider) stopWatchDisplay).getSeconds(),
								((TimeProvider) stopWatchDisplay).getMilliSeconds());
						stopWatchDisplay.setTimeProvider(TimeCounter.getInstance());
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

			final LedButton counterMode = new LedButton("\nToggle counter mode between in- or decrement (default).");
			counterMode.setSkinText("<");
			counterMode.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue)
					{
						counterMode.setSkinText(">");
						TimeCounter.setMode(CounterMode.UP);
					}
					else
					{
						counterMode.setSkinText("<");
						TimeCounter.setMode(CounterMode.DOWN);
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

			LedButton resizeBtn = new LedButton("\nMinimize or maximize window");
			resizeBtn.setSkinText("|");
			resizeBtn.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue)
					{
						setCurrentHeightToStage(stage.getMaxHeight());
						setCurrentWidthToStage(stage.getMaxWidth());
						resizeBtn.setSkinText("o");
					}
					else
					{
						initScreen();
						resizeBtn.setSkinText("|");
					}
				}
			});
			add(resizeBtn, 5, 0);

			final LedButton closeBtn = new LedButton("Quit");
			closeBtn.setSkinText("X");
			closeBtn.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					Platform.exit();
				}
			});
			add(closeBtn, 6, 0);
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


	private void initScreen()
	{
		if (mainContainer.getActScreen() == WATCH_SCREEN2)
		{
			watchLines.get(0).pause();
			watchLines.get(1).play();

			setCurrentWidthToStage(240);
			setCurrentHeightToStage(290);
		}
		else if (mainContainer.getActScreen() == COUNTER_SCREEN)
		{
			watchLines.get(0).pause();
			watchLines.get(1).pause();

			setCurrentWidthToStage(225);
			setCurrentHeightToStage(115);
		}
		else
		{
			watchLines.get(1).pause();
			watchLines.get(0).play();

			setCurrentWidthToStage(225);
			setCurrentHeightToStage(115);
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
