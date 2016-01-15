package application.gui.ledclock;

import application.gui.ScreenNode;
import application.gui.TimeProvider;
import application.gui.segment.TimeConsumer;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

public class LedControl extends Control implements TimeConsumer, ScreenNode
{
	private LedClockMultiplexer multiPlexerM = new LedClockMultiplexer();
	private LedClockMultiplexer multiPlexerS = new LedClockMultiplexer();
	private LedClockMultiplexer multiPlexerH = new LedClockMultiplexer();
	private TimeProvider timeProvider;
	private ObjectProperty<Color> ledColor;
	private int lastSec;
	private int lastMinute;
	private int lastHour;
	private StringProperty text;


	public LedControl(TimeProvider provider)
	{
		getStyleClass().add("ledclock");
		ledColor = new SimpleObjectProperty<Color>(Color.SILVER);
		text = new SimpleStringProperty(this, "text", "test");
		setTimeProvider(provider);
		setPrefHeight(280);
		setPrefWidth(235);
	}


	public void addMinutesListener(InvalidationListener toAdd)
	{
		multiPlexerM.getProperty().addListener(toAdd);
		setMinutes(timeProvider.getMinutes());
	}


	public void addSecondsChangedListener(ChangeListener<Integer> toAdd)
	{
		multiPlexerS.getProperty().addListener(toAdd);
	}


	public void addHoursListener(InvalidationListener toAdd)
	{
		multiPlexerH.getProperty().addListener(toAdd);
		setHours(timeProvider.getHours());
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
		int actMinutes = timeProvider.getMinutes();
		int actHour = timeProvider.getHours();
		if (lastSec != actSec)
		{
			setSeconds(actSec);
			lastSec = actSec;
		}
		if (lastMinute != actMinutes)
		{
			setMinutes(actMinutes);
			lastMinute = actMinutes;
		}
		if (lastHour != actHour)
		{
			setHours(actHour);
			lastHour = actHour;
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


	@Override
	public Node getNode()
	{
		return this;
	}

}
