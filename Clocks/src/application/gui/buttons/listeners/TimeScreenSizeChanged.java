package application.gui.buttons.listeners;

import application.TimeScreen;
import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;

public abstract class TimeScreenSizeChanged implements ChangeListener<TimeScreen>
{
	protected final Stage stage;


	public TimeScreenSizeChanged(Stage stage)
	{
		this.stage = stage;
	}
}
