package application.gui.buttons.listeners;

import application.counter.Counter;
import application.counter.CounterMode;
import javafx.beans.value.ObservableValue;

public final class CounterModeEvent extends CounterEvent
{

	public CounterModeEvent(Counter controller)
	{
		super(controller);
	}


	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		counter.setCountMode(newValue ? CounterMode.INCREMENT : CounterMode.DECREMENT);
	}
}