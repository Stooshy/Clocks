package application.gui.buttons.listeners;

import application.gui.Updateable;
import javafx.beans.value.ObservableValue;

public final class StartUpdate extends UpdateableEvent
{

	public StartUpdate(Updateable controller)
	{
		super(controller);
	}


	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		if (updateable.isRunning())
			updateable.pauseUpdate();
		else
			updateable.startUpate();
	}
}