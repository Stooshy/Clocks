package application.gui.buttons.listeners;

import application.counter.Counter;
import javafx.beans.value.ChangeListener;

public abstract class CounterEvent implements ChangeListener<Boolean>
{
	protected final Counter counter;


	public CounterEvent(Counter counter)
	{
		this.counter = counter;
	}
}
