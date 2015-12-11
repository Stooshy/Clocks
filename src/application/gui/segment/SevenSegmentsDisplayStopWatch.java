package application.gui.segment;

public class SevenSegmentsDisplayStopWatch extends SevenSegmentsDisplay implements TimeConsumer
{

	public SevenSegmentsDisplayStopWatch()
	{
		addSegements();
	}


	@Override
	public int getMilliseconds()
	{
		return getSegments().get(getSegments().size() - 3).getDigit().ordinal() * 100
				+ getSegments().get(getSegments().size() - 2).getDigit().ordinal() * 10
				+ getSegments().get(getSegments().size() - 1).getDigit().ordinal();
	}


	public void setMilliseconds(int value)
	{
		int a = value % 10;
		int b = (value / 10) % 10;
		int c = (value / 100) % 10;

		getSegments().get(getSegments().size() - 1).set(SevenDigit.values()[a]);
		getSegments().get(getSegments().size() - 2).set(SevenDigit.values()[b]);
		getSegments().get(getSegments().size() - 3).set(SevenDigit.values()[c]);
	}


	private void addSegements()
	{
//		for (int colIdx = 1; colIdx <= 3; colIdx++)
//		{
//			final SevenSegmentsSmall seg = new SevenSegmentsSmall();
//			super.addToPane(seg, colIdx);
//		}
	}


	public void setTime(int seconds, int minutes, int hours, int milli)
	{
		setMilliseconds(milli);
		setTime(seconds, minutes, hours);
	}


	@Override
	public void consumeTime()
	{
		setMilliseconds(timeProvider.getMilliseconds());
		setTime(timeProvider.getSeconds(), timeProvider.getMinutes(), timeProvider.getHours());
	}

}
