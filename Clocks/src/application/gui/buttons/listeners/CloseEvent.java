package application.gui.buttons.listeners;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public final class CloseEvent implements ChangeListener<Boolean>
{
	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		Platform.exit();
	}
}
