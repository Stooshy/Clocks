package application;

import application.counter.TimeCounter;
import application.gui.ledclock.LedControl;
import application.gui.ledmatrix.LedMatrixControl;
import application.gui.segment.SevenSegmentsDisplay;
import application.gui.segment.SevenSegmentsDisplayStopWatch;
import javafx.scene.Node;
import javafx.util.Duration;

public enum TimeScreen
{
	//@formatter:off
	COUNTER_SCREEN(new SevenSegmentsDisplayStopWatch(new TimeCounter(Duration.millis(100)))),
	SEVENSEG( new SevenSegmentsDisplay(new ObserveableTimeProvider(Duration.millis(951)))),
	LED(new LedControl(new ObserveableTimeProvider(Duration.millis(951)))),
	MATRIX(new LedMatrixControl(new ObserveableTimeProvider(Duration.millis(951))));
	//@formatter:on

	public final Object screen;


	public Node getNode()
	{
		return (Node) this.screen;
	}


	private TimeScreen(Object screen)
	{
		this.screen = screen;
	}


	public TimeScreen next()
	{
		return values()[(ordinal() + 1) % values().length];
	}
}
