package application.gui.ledclock;

import javafx.beans.property.SimpleObjectProperty;

public class LedClockMultiplexer
{
	private final SimpleObjectProperty<Integer> valueProperty;


	public LedClockMultiplexer()
	{
		valueProperty = new SimpleObjectProperty<Integer>();
	}


	public void set(int value)
	{
		getProperty().set(new Integer(value));
	}


	public int getNumber()
	{
		return valueProperty.get();
	}


	public SimpleObjectProperty<Integer> getProperty()
	{
		return valueProperty;
	}

}
