package main;

import static application.TimeScreen.COUNTER_SCREEN;

import java.util.List;

import application.TimeScreen;
import application.counter.Counter;
import application.geodata.DataFile;
import application.geodata.ImageGeoLocation;
import application.gui.buttons.Buttons;
import application.gui.buttons.listeners.TimeScreenMax;
import application.gui.buttons.listeners.TimeScreenPref;
import application.gui.info.RotateablePane;
import application.gui.info.WeatherPane;
import application.gui.screen.ScreensController;
import argument.ArgumentInterpreter;
import argument.ArgumentInterpreter.Arguments;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Clocks extends Application
{
	public static void main(String[] args)
	{
		Application.launch(args);
	}


	@Override
	public void start(Stage stage)
	{
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.getIcons().add(new Image(Clocks.class.getResourceAsStream("/res/logo.png")));
		stage.setTitle("7 Segments");
		BorderPane borderPane = new BorderPane();
		borderPane.setId("ROOTNODE");
		ScreensController mainContainer = new ScreensController();
		borderPane.setCenter(mainContainer.getPane());

		Scene scene = new Scene(borderPane, 0, 0);
		scene.getStylesheets().add(Clocks.class.getResource("/main/clocks.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);

		ArgumentInterpreter interpreter = new ArgumentInterpreter(getParameters().getUnnamed());
		switch (interpreter.getScreenSize())
		{
		case MAX:
			initMax(borderPane);
			mainContainer.addScreen(TimeScreen.LED);
			mainContainer.registerScreenChangedListener(new TimeScreenMax(stage));
			break;
		case PREF:
			initPref(stage, borderPane, mainContainer);
			@SuppressWarnings("unchecked")
			List<Integer> parameters = (List<Integer>) interpreter.get(Arguments.SCREENS).getParameters();
			for (Integer parameter : parameters)
			{
				mainContainer.addScreen(TimeScreen.values()[parameter - 1]);
			}
			mainContainer.registerScreenChangedListener(new TimeScreenPref(stage));
		default:
			break;

		}

		addMouseListeners(stage, mainContainer);
		stage.show();
		mainContainer.setFirstScreen();
	}


	private void initPref(final Stage stage, BorderPane borderPane, ScreensController controller)
	{
		final Buttons buttons = new Buttons(controller, stage);
		buttons.setPrefHeight(40);
		borderPane.setTop(buttons);
	}


	private void initMax(BorderPane borderPane)
	{
		RotateablePane pane = new RotateablePane();
		DataFile data = new DataFile(System.getProperty("user.dir") + "/locations.txt");
		for (ImageGeoLocation location : data.locations)
		{
			pane.addPane(createInfoNode(location.latitude, location.longitude, location.imageUrl));
		}
		borderPane.setRight(pane);
	}


	private Pane createInfoNode(float latitude, float longitude, String imageUrl)
	{
		WeatherPane wp = new WeatherPane(Duration.seconds(900), latitude, longitude, imageUrl);
		wp.setPrefWidth(256);
		return wp;
	}


	private void addMouseListeners(final Stage stage, ScreensController controller)
	{
		for (Node nodeToAdd : controller.getScreens())
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
							if (controller.isActualScreen(COUNTER_SCREEN))
							{
								((Counter) TimeScreen.COUNTER_SCREEN.screen).set(0, 0, 0);
							}
						}
					}
				}
			});
		}
	}

	private class Delta
	{
		double x, y;
	}

}
