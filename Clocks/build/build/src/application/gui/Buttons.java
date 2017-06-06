package application.gui;

import static application.TimeScreen.COUNTER_SCREEN;

import application.TimeScreen;
import application.Timer;
import application.counter.TimeCounter;
import application.gui.ledbutton.LedButton;
import application.gui.screen.ScreensController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Buttons extends GridPane
{

	public Buttons(Stage stage, ScreensController mainContainer)
	{
		setId("TOPPANE");

		LedButton showCounter = new LedButton("\nSwitch between displays.");
		showCounter.setSkinText("M");

		showCounter.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				((Updateable) mainContainer.getScreen().screen).pauseUpdate();
				mainContainer.setNextScreen();
				if (mainContainer.getScreen() != TimeScreen.COUNTER_SCREEN)
				{
					((Updateable) mainContainer.getScreen().screen).startUpate();
				}
			}
		});
		int idx = 0;
		add(showCounter, idx++, 0);

		LedButton go = new LedButton(
				"\nStart or stop counter.\nIf the counter is stopped douple click on display will reset to 0.\nClick on digit to change value");
		go.setSkinText("Go");
		go.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (mainContainer.getScreen().equals(COUNTER_SCREEN))
				{
					if (((Updateable) mainContainer.getScreen().screen).isRunning())
					{
						((Updateable) mainContainer.getScreen().screen).pauseUpdate();
					}
					else
					{
						Timer counter = (Timer) mainContainer.getScreen().screen;
						counter.set(counter.getMinutes(), counter.getSeconds(), counter.getMilliSeconds());

						((Updateable) mainContainer.getScreen().screen).startUpate();
					}
				}
			}
		});
		add(go, idx++, 0);

		final LedButton counterMode = new LedButton("\nToggle counter mode between in- or decrement (default).");
		counterMode.setSkinText("<");
		counterMode.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if(mainContainer.getScreen() != TimeScreen.COUNTER_SCREEN)
					return;
				
				if (newValue)
				{
					counterMode.setSkinText(">");
					((TimeCounter) mainContainer.getScreen().screen).setMode(CounterMode.UP);
				}
				else
				{
					counterMode.setSkinText("<");
					((TimeCounter) mainContainer.getScreen().screen).setMode(CounterMode.DOWN);
				}
			}
		});

		add(counterMode, idx++, 0);

		LedButton resizeBtn = new LedButton("\nMinimize or maximize window");
		resizeBtn.setSkinText("|");
		resizeBtn.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue)
				{
					stage.setHeight(mainContainer.getMaxHeight() + ((BorderPane) stage.getScene().getRoot())
							.topProperty().get().getBoundsInLocal().getHeight());
					stage.setWidth(mainContainer.getMaxWidth());
					resizeBtn.setSkinText("o");
				}
				else
				{
					stage.setHeight(mainContainer.getPrefHeight() + ((BorderPane) stage.getScene().getRoot())
							.topProperty().get().getBoundsInLocal().getHeight());
					stage.setWidth(mainContainer.getPrefWidth());
					resizeBtn.setSkinText("|");
				}
			}
		});
		add(resizeBtn, idx++, 0);

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
		add(closeBtn, idx++, 0);
	}

}
