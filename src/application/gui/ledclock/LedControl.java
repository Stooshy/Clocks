package application.gui.ledclock;

import java.util.BitSet;

import application.gui.TimeProvider;
import application.gui.segment.TimeConsumer;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

public class LedControl extends Control implements TimeConsumer
{
	private LedClockMultiplexer multiPlexerM = new LedClockMultiplexer(60);
	private LedClockMultiplexer multiPlexerS = new LedClockMultiplexer(60);
	private LedClockMultiplexer multiPlexerH = new LedClockMultiplexer(12);
	private TimeProvider timeProvider;
	private ObjectProperty<Color> ledColor;
	private int lastSec;


	public LedControl()
	{
		getStyleClass().add("ledclock");
		setPadding(new Insets(5, 5, 5, 5));
		ledColor = new SimpleObjectProperty<>(Color.YELLOW);
	}


	public void addMinutesListener(InvalidationListener toAdd)
	{
		multiPlexerM.getBitsProperty().addListener(toAdd);
	}


	public void addSecondsListener(InvalidationListener toAdd)
	{
		multiPlexerS.getBitsProperty().addListener(toAdd);
	}


	public void addSecondsChangedListener(ChangeListener<BitSet> toAdd)
	{
		multiPlexerS.getBitsProperty().addListener(toAdd);
	}


	public void addHoursListener(InvalidationListener toAdd)
	{
		multiPlexerH.getBitsProperty().addListener(toAdd);
	}


	public void count(boolean up)
	{
		setMinutes(multiPlexerM.getNumber() + 1);
	}


	public int getValue()
	{
		return multiPlexerM.getNumber();
	}


	public void consumeTime()
	{
		if (lastSec < timeProvider.getSeconds())
			setSeconds(timeProvider.getSeconds());
		setMinutes(timeProvider.getMinutes());
		setHours(timeProvider.getHours());
		lastSec = timeProvider.getSeconds();
	}


	private void setSeconds(int value)
	{
		multiPlexerS.set(value);
	}


	private void setMinutes(int value)
	{
		multiPlexerM.set(value+1);
	}


	private void setHours(int value)
	{
		multiPlexerH.set(value);
	}


	public void setTimeProvider(TimeProvider provider)
	{
		this.timeProvider = provider;
		consumeTime();
	}


	public final Color getLedColor()
	{
		return ledColor.get();
	}


	public final void setLedColor(final Color LED_COLOR)
	{
		ledColor.set(LED_COLOR);
	}


	public final ObjectProperty<Color> ledColorProperty()
	{
		return ledColor;
	}
}
