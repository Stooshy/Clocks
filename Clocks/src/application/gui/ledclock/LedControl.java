package application.gui.ledclock;

import application.ObserveableTimeProvider;
import application.gui.Updateable;
import application.gui.screen.ScreenNode;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

public final class LedControl extends Control implements ScreenNode, Updateable
{
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = 1024;
	public static final double PREF_SIZE = 276;
	private ObjectProperty<Color> ledColor;
	private ObserveableTimeProvider provider;


	public LedControl(ObserveableTimeProvider provider)
	{
		getStyleClass().add("ledclock");
		init();
		setTimeProvider(provider);
		ledColor = new SimpleObjectProperty<Color>(Color.SILVER);
	}


	private void init()
	{
		if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0
				|| Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0)
		{
			setPrefSize(PREF_SIZE, PREF_SIZE);
		}

		if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0)
		{
			setMinSize(PREF_SIZE, PREF_SIZE);
		}

		if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0)
		{
			setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHTS);
		}
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


	@Override
	public void pauseUpdate()
	{
		provider.pauseUpdate();
	}

	@Override
	public void startUpate()
	{
		provider.startUpate();
	}


	@Override
	public boolean isRunning()
	{
		return provider.isRunning();
	}


	final void addMinutesListener(InvalidationListener toAdd)
	{
		provider.addMinutesListener(toAdd);
	}


	final void addSecondsChangedListener(InvalidationListener toAdd)
	{
		provider.addSecondsInvalidationListener(toAdd);
	}


	final void addHoursListener(InvalidationListener toAdd)
	{
		provider.addHoursListener(toAdd);
	}


	public void setTimeProvider(ObserveableTimeProvider provider)
	{
		this.provider = provider;
	}
	
	@Override
	public void toggleMode()
	{
		if (isRunning())
		{
			pauseUpdate();
		}
		else
		{
			startUpate();
		}
	}
}
