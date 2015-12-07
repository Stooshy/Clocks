package application;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import application.gui.ledbutton.LedButton;
import application.gui.ledbutton.TimeCounter;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.TimeProvider;
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
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	private SevenSegmentsDisplay display;
	private TimeCounter counter;
	private LedButton showCounter;
	private Timeline counterLine;
	private LedButton go;


	@Override
	public void init()
	{
		display = new SevenSegmentsDisplay();
		display.setTimeProvider(new LocalTimeProvider());
		counter = new TimeCounter(0, 0, 10);
	}


	@Override
	public void start(Stage stage)
	{
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);

		final BorderPane borderPane = new BorderPane();
		borderPane.setId("ROOTNODE");

		Rectangle rect = new Rectangle(1024, 768);
		rect.setArcHeight(25.0);
		rect.setArcWidth(25.0);
		borderPane.setClip(rect);

		borderPane.setTop(new Buttons());
		Pane pane = display.getPane();
		pane.setId("clockpane");
		borderPane.setCenter(pane);

		final Delta dragDelta = new Delta();

		Scene scene = new Scene(borderPane, 200, 100);
		scene.getStylesheets().add(getClass().getResource("7segmentdemo.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		pane.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				// record a delta distance for the drag and drop operation.
				dragDelta.x = stage.getX() - mouseEvent.getScreenX();
				dragDelta.y = stage.getY() - mouseEvent.getScreenY();
				scene.setCursor(Cursor.MOVE);
			}
		});

		pane.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				stage.setX(event.getScreenX() + dragDelta.x);
				stage.setY(event.getScreenY() + dragDelta.y);
			}
		});

		pane.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});

		borderPane.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				// record a delta distance for the drag and drop operation.
				dragDelta.x = stage.getX() - mouseEvent.getScreenX();
				dragDelta.y = stage.getY() - mouseEvent.getScreenY();
				scene.setCursor(Cursor.MOVE);
			}
		});

		borderPane.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				stage.setX(event.getScreenX() + dragDelta.x);
				stage.setY(event.getScreenY() + dragDelta.y);
			}
		});

		borderPane.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});

		borderPane.setOnMouseClicked(new EventHandler<MouseEvent>()
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
								counter = new TimeCounter(0, 0, 0);
								display.setTime(0, 0, 0);
								display.setTimeProvider(display);
							}
						}
					}
				}
			}
		});

		final Timeline displayLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				display.consumeTime();
			}
		}), new KeyFrame(Duration.millis(200)));
		displayLine.setCycleCount(Animation.INDEFINITE);
		displayLine.play();

		counterLine = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				if (!counter.decrement())
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
		}), new KeyFrame(Duration.millis(1000)));
		counterLine.setCycleCount(Animation.INDEFINITE);
		stage.show();
	}


	public static void main(String[] args)
	{
		Application.launch(args);
	}

	class Buttons extends GridPane
	{

		public Buttons()
		{
			final Glow glow = new Glow();
			setId("TOPPANE");
			showCounter = new LedButton("\nShow daytime or counter.");
			showCounter.setText("M");
			showCounter.setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					showCounter.setEffect(glow);
				}
			});
			showCounter.setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					showCounter.setEffect(null);
				}
			});
			showCounter.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (newValue)
					{
						if ((counterLine.getStatus() == Status.STOPPED))
						{
							display.setTime(counter.getSeconds(), counter.getMinutes(), counter.getHours());
							display.setTimeProvider(display);
						}
						else
							display.setTimeProvider(counter);
					}
					else
					{
						display.setTimeProvider(new LocalTimeProvider());
					}
				}
			});
			add(showCounter, 0, 0);

			go = new LedButton();
			go.setText("Go");
			go.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					if (counterLine.getStatus() == Status.STOPPED)
					{
						counter = new TimeCounter(display.getHours(), display.getMinutes(), display.getSeconds());
						display.setTimeProvider(counter);
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
			go.setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					go.setEffect(glow);
				}
			});
			go.setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					go.setEffect(null);
				}
			});
			add(go, 1, 0);

			String date = LocalDate.now()
					.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.GERMAN));
			Font font = Font.font("Tahoma", FontWeight.THIN, FontPosture.ITALIC, 11.0);
			Text text = new Text(date);
			text.setFont(font);
			text.setTextOrigin(VPos.TOP);
			text.setTextAlignment(TextAlignment.LEFT);
			text.setWrappingWidth(100);
			add(text, 2, 0);

			LedButton closeBtn = new LedButton();
			closeBtn.setText("X");
			closeBtn.selectedProperty().addListener(new ChangeListener<Boolean>()
			{
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
				{
					Platform.exit();
				}
			});
			closeBtn.setOnMouseEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					closeBtn.setEffect(glow);
				}
			});
			closeBtn.setOnMouseExited(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent mouseEvent)
				{
					closeBtn.setEffect(null);
				}
			});
			add(closeBtn, 3, 0);

			ColumnConstraints col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.CENTER);
			getColumnConstraints().add(col);

			col = new ColumnConstraints();
			col.setHalignment(HPos.LEFT);
			col.setFillWidth(true);
			col.setPrefWidth(90);
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

	private class LocalTimeProvider implements TimeProvider
	{

		@Override
		public int getSeconds()
		{
			return LocalTime.now().getSecond();
		}


		@Override
		public int getMinutes()
		{
			return LocalTime.now().getMinute();
		}


		@Override
		public int getHours()
		{
			return LocalTime.now().getHour();
		}
	}
}
