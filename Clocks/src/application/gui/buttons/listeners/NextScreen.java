package application.gui.buttons.listeners;

import application.gui.screen.ScreensController;
import javafx.beans.value.ObservableValue;

public final class NextScreen extends ScreenControlEvent
{

	public NextScreen(ScreensController controller)
	{
		super(controller);
	}


	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		controller.setNextScreen();
	}
}
