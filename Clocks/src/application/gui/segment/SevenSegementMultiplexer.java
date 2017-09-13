package application.gui.segment;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

class SevenSegementMultiplexer
{
	private SimpleObjectProperty<boolean[]> segments;
	private static int noOfSegments = 7;


	public SevenSegementMultiplexer()
	{
		segments = new SimpleObjectProperty<boolean[]>(new boolean[noOfSegments]);
	}


	private static boolean[] convert(int value)
	{
		boolean[] valueBits = new boolean[noOfSegments];
		int index = 0;
		while (value != 0)
		{
			if (value % 2 != 0)
			{
				valueBits[index] = true;
			}
			++index;
			value = value >>> 1;
		}
		return valueBits;
	}


	public void set(SevenDigit value)
	{
		segments.set(convert(value.getCode()));
	}


	private int getCode()
	{
		int value = 0;
		for (int i = 0; i < segments.get().length; ++i)
		{
			value += segments.get()[i] ? (1 << i) : 0;
		}
		return (value == 0) ? 0 : SevenDigit.findDigit(value).getCode();
	}


	public void addListener(InvalidationListener toAdd)
	{
		segments.addListener(toAdd);
	}


	public SevenDigit getSevenDigit()
	{
		int code = getCode();
		if (code != 0)
			return SevenDigit.findDigit(code);
		else
			return SevenDigit.ZERO;
	}


	public SevenDigit getNextNumber()
	{
		return getSevenDigit().nextNumber(true);
	}
}
