package application.gui.ledclock;

import java.util.BitSet;

import application.gui.TimeProvider;
import application.gui.segment.TimeConsumer;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
	private int lastHour;
	private StringProperty text;


	public LedControl(TimeProvider provider)
	{
		getStyleClass().add("ledclock");
		setPadding(new Insets(5, 5, 5, 5));
		ledColor = new SimpleObjectProperty<Color>(Color.SILVER);
		text = new SimpleStringProperty(this, "text", "test");
		setTimeProvider(provider);
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


	public String getSkinText()
	{
		return text.get();
	}


	public void setSkinText(String value)
	{
		text.set(value);
	}


	public final StringProperty textSkinProperty()
	{
		return text;
	}


	public void count(boolean up)
	{
		setMinutes(multiPlexerM.getNumber() + 1);
	}


	public void consumeTime()
	{
		int actSec = timeProvider.getSeconds();
		if (lastSec != actSec)
		{
			setSeconds(actSec);
			setMinutes(timeProvider.getMinutes());
			lastSec = actSec;
		}
		if (lastHour != timeProvider.getHours())
		{
			setHours(timeProvider.getHours());
			lastHour = timeProvider.getHours();
		}
	}


	private void setSeconds(int value)
	{
		setSkinText("" + value);
		multiPlexerS.set(value);
	}


	private void setMinutes(int value)
	{
		multiPlexerM.set(value + 1);
	}


	private void setHours(int value)
	{
		multiPlexerH.set(value % 12);
	}


	public void setTimeProvider(TimeProvider provider)
	{
		this.timeProvider = provider;
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
