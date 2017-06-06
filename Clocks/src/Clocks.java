
import static application.TimeScreen.COUNTER_SCREEN;
import static application.TimeScreen.LED;
import static application.TimeScreen.MATRIX;
import static application.TimeScreen.SEVENSEG;

import java.util.List;

import application.TimeScreen;
import application.Timer;
import application.gui.Buttons;
import application.gui.info.RotateablePane;
import application.gui.info.WeatherPane;
import application.gui.screen.ScreensController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	private ScreensController mainContainer;


	public static void main(String[] args)
	{
		Application.launch(args);
	}


	@Override
	public void init()
	{
		List<String> parameters = getParameters().getUnnamed();
		int valueIDX = parameters.indexOf("-max");
		if (valueIDX > -1)
		{
			mainContainer = new ScreensController(LED);
		}
		else
			mainContainer = new ScreensController(LED, SEVENSEG, MATRIX, COUNTER_SCREEN);
	}


	@Override
	public void start(Stage stage)
	{
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.getIcons().add(new Image(Clocks.class.getResourceAsStream("res/logo.png")));
		stage.setTitle("7 Segments");
		BorderPane borderPane = new BorderPane();
		borderPane.setId("ROOTNODE");
		borderPane.setCenter(mainContainer.getActualScreen());
		addMouseListeners(stage, borderPane);

		Scene scene = new Scene(borderPane, 0, 0);
		scene.getStylesheets().add(getClass().getResource("clocks.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);

		addMouseListeners(stage, mainContainer.getScreens());

		List<String> parameters = getParameters().getUnnamed();
		int valueIDX = parameters.indexOf("-max");
		if (valueIDX > -1)
		{
			initMax(stage, borderPane);
			stage.setFullScreen(true);
		}
		else
		{
			initPref(stage, borderPane);
		}
		stage.show();
		mainContainer.setScreen(TimeScreen.LED);
	}


	private void initPref(final Stage stage, BorderPane borderPane)
	{
		mainContainer.registerScreenChangedListener(new ChangeListener<TimeScreen>()
		{
			@Override
			public void changed(ObservableValue<? extends TimeScreen> observable, TimeScreen oldValue,
					TimeScreen newValue)
			{
				double buttonHeight = ((BorderPane) stage.getScene().getRoot()).topProperty().get().getBoundsInParent()
						.getHeight();
				stage.setHeight(mainContainer.getPrefHeight() + buttonHeight);
				stage.setWidth(mainContainer.getPrefWidth());
			}
		});
		Buttons buttons = new Buttons(stage, mainContainer);
		buttons.setPrefHeight(40);
		borderPane.setTop(buttons);
	}


	private void initMax(final Stage stage, BorderPane borderPane)
	{
		mainContainer.registerScreenChangedListener(new ChangeListener<TimeScreen>()
		{
			@Override
			public void changed(ObservableValue<? extends TimeScreen> observable, TimeScreen oldValue,
					TimeScreen newValue)
			{
				double infoWidth = ((BorderPane) stage.getScene().getRoot()).rightProperty().get().getBoundsInLocal()
						.getWidth();
				stage.setWidth(mainContainer.getMaxWidth() + infoWidth);
				stage.setHeight(mainContainer.getMaxHeight());
			}
		});
		Pane pi1 = createInfoNode(53.65f, 9.9f);
		Pane pi2 = createInfoNode(48.37f, 10.88f);
		RotateablePane pane = new RotateablePane();
		pane.addChild(pi1, pi2);
		borderPane.setRight(pane);
	}


	private Pane createInfoNode(float latitude, float longitude)
	{
		WeatherPane wp = new WeatherPane(Duration.seconds(900), latitude, longitude);
		wp.setPrefWidth(256);
		return wp;
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
							if (mainContainer.getScreen().equals(COUNTER_SCREEN))
							{
								((Timer) mainContainer.getScreen().screen).set(0, 0, 0);
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
