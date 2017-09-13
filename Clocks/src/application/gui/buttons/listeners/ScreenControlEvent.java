package application.gui.buttons.listeners;

import application.gui.screen.ScreensController;
import javafx.beans.value.ChangeListener;

public abstract class ScreenControlEvent implements ChangeListener<Boolean>
{
	protected final ScreensController controller;


	public ScreenControlEvent(ScreensController controller)
	{
		this.controller = controller;
	}
}
