package application.gui.buttons.listeners;

import application.gui.Updateable;
import javafx.beans.value.ChangeListener;

public abstract class UpdateableEvent implements ChangeListener<Boolean>
{
	protected Updateable updateable;

	public UpdateableEvent(Updateable updateable)
	{
		this.updateable = updateable;
	}

}
