package application.gui.buttons;

import application.TimeScreen;
import application.counter.Counter;
import application.gui.Updateable;
import application.gui.buttons.ledbutton.LedButton;
import application.gui.buttons.listeners.CloseEvent;
import application.gui.buttons.listeners.CounterModeEvent;
import application.gui.buttons.listeners.NextScreen;
import application.gui.buttons.listeners.ResizeEvent;
import application.gui.buttons.listeners.StartUpdate;
import application.gui.screen.ScreensController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Buttons extends GridPane
{
	public Buttons(ScreensController mainContainer, Stage stage)
	{
		this();
		addDisplayChangeListener(mainContainer);
		addModeChangeListener((Counter) TimeScreen.COUNTER_SCREEN.getNode());
		addStartListener((Updateable) TimeScreen.COUNTER_SCREEN.getNode());
		addResizeChangeListener(mainContainer, stage);
	}


	public Buttons()
	{
		setId("TOPPANE");

		LedButton showCounter = new LedButton("\nSwitch between displays.");
		showCounter.setSkinText("M");
		int idx = 0;
		add(showCounter, idx++, 0);

		LedButton go = new LedButton(
				"\nStart or stop counter.\nIf the counter is stopped douple click on display will reset to 0.\nClick on digit to change value");
		go.setSkinText("Go");
		add(go, idx++, 0);

		final LedButton counterMode = new LedButton("\nToggle counter mode between in- or decrement (default).");
		counterMode.addSelectedListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue)
				{
					counterMode.setSkinText(">");
				}
				else
				{
					counterMode.setSkinText("<");
				}
			}
		});

		counterMode.setSkinText("<");
		add(counterMode, idx++, 0);

		LedButton resizeBtn = new LedButton("\nMinimize or maximize window");
		resizeBtn.addSelectedListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue)
				{
					resizeBtn.setSkinText("o");
				}
				else
				{
					resizeBtn.setSkinText("|");
				}
			}
		});
		resizeBtn.setSkinText("|");
		add(resizeBtn, idx++, 0);

		final LedButton closeBtn = new LedButton("Quit");
		closeBtn.addSelectedListener(new CloseEvent());
		closeBtn.setSkinText("X");
		add(closeBtn, idx++, 0);
	}


	void addDisplayChangeListener(ScreensController controller)
	{
		((LedButton) getChildren().get(0)).addSelectedListener(new NextScreen(controller));
	}


	void addStartListener(Updateable controller)
	{
		((LedButton) getChildren().get(1)).addSelectedListener(new StartUpdate(controller));
	}


	void addModeChangeListener(Counter controller)
	{
		((LedButton) getChildren().get(2)).addSelectedListener(new CounterModeEvent(controller));
	}


	void addResizeChangeListener(ScreensController controller, Stage stage)
	{
		((LedButton) getChildren().get(3)).addSelectedListener(new ResizeEvent(controller, stage));
	}

}
