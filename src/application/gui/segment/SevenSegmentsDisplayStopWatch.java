package application.gui.segment;

import application.counter.TimeCounter;

public class SevenSegmentsDisplayStopWatch extends SevenSegmentsDisplay implements TimeConsumer
{

	public SevenSegmentsDisplayStopWatch()
	{
		super(TimeCounter.getInstance());
	}


	@Override
	public int getSeconds()
	{
		return getSegment(2).getValueDisplayed() * 10 + getSegment(3).getValueDisplayed();
	}


	@Override
	public int getMinutes()
	{
		return getSegment(0).getValueDisplayed() * 10 + getSegment(1).getValueDisplayed();
	}


	@Override
	public int getHours()
	{
		return 0;
	}


	private void setSeconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(2).set(SevenDigit.values()[b]);
		getSegment(3).set(SevenDigit.values()[a]);
	}


	private void setMinutes(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;

		getSegment(0).set(SevenDigit.values()[b]);
		getSegment(1).set(SevenDigit.values()[a]);
	}


	@Override
	public int getMilliSeconds()
	{
		return getSegment(4).getValueDisplayed() * 100 + getSegment(5).getValueDisplayed() * 10;
	}


	private void setMilliseconds(int value)
	{
		int b = (value / 10) % 10;
		int c = (value / 100) % 10;

		getSegment(5).set(SevenDigit.values()[b]);
		getSegment(4).set(SevenDigit.values()[c]);
	}


	@Override
	public void consumeTime()
	{
		setMilliseconds(timeProvider.getMilliSeconds());
		setSeconds(timeProvider.getSeconds());
		setMinutes(timeProvider.getMinutes());
	}

}
