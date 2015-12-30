package application;

import application.gui.ScreenNode;
import application.gui.TimeProvider;
import application.gui.ledclock.LedControl;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.SevenSegmentsDisplayStopWatch;
import application.gui.segment.TimeConsumer;
import javafx.scene.Node;

public enum TimeScreen implements TimeProvider,TimeConsumer
{
	//@formatter:off
	COUNTER_SCREEN(new SevenSegmentsDisplayStopWatch()),
	WATCH_SCREEN1(new SevenSegmentsDisplay(LocalTimeProvider.getInstance())),
	WATCH_SCREEN2(new LedControl(LocalTimeProvider.getInstance()));
	//@formatter:on

	protected final ScreenNode consumer;


	public void consumeTime()
	{
		((TimeConsumer) this.consumer).consumeTime();
	}


	public Node getNode()
	{
		return this.consumer.getNode();
	}


	private TimeScreen(ScreenNode consumer)
	{
		this.consumer = consumer;
	}


	public TimeScreen nextScreen()
	{
		return values()[(ordinal() + 1) % values().length];
	}


	@Override
	public int getSeconds()
	{
		return ((TimeProvider) this.consumer).getSeconds();
	}


	@Override
	public int getMinutes()
	{
		return ((TimeProvider) this.consumer).getMinutes();
	}


	@Override
	public int getHours()
	{
		return ((TimeProvider) this.consumer).getHours();
	}


	@Override
	public int getMilliSeconds()
	{
		return ((TimeProvider) this.consumer).getMilliSeconds();
	}


	@Override
	public void setTimeProvider(TimeProvider provider)
	{
		((TimeConsumer) this.consumer).setTimeProvider(provider);
	}

}
