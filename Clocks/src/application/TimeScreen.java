package application;

import application.counter.TimeCounter;
import application.gui.TimeProvider;
import application.gui.ledclock.LedControl;
import application.gui.ledmatrix.LedMatrixControl;
import application.gui.screen.ScreenNode;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.SevenSegmentsDisplayStopWatch;
import javafx.scene.Node;
import javafx.util.Duration;

public enum TimeScreen implements TimeProvider
{
	//@formatter:off
	COUNTER_SCREEN(new SevenSegmentsDisplayStopWatch(new TimeCounter(Duration.millis(100)))),
	SEVENSEG(new SevenSegmentsDisplay(new ObserveableTimeProvider(Duration.millis(951)))),
	LED(new LedControl(new ObserveableTimeProvider(Duration.millis(951)))),
	MATRIX(new LedMatrixControl(new ObserveableTimeProvider(Duration.millis(951))));
	//@formatter:on

	public final ScreenNode screen;


	public Node getNode()
	{
		return this.screen.getNode();
	}


	private TimeScreen(ScreenNode screen)
	{
		this.screen = screen;
	}


	public TimeScreen next()
	{
		return values()[(ordinal() + 1) % values().length];
	}


	@Override
	public int getSeconds()
	{
		return ((TimeProvider) this.screen).getSeconds();
	}


	@Override
	public int getMinutes()
	{
		return ((TimeProvider) this.screen).getMinutes();
	}


	@Override
	public int getHours()
	{
		return ((TimeProvider) this.screen).getHours();
	}


	@Override
	public int getMilliSeconds()
	{
		return ((TimeProvider) this.screen).getMilliSeconds();
	}
}
