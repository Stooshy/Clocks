package application.gui.segment;

import java.util.BitSet;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

public class SevenSegementMultiplexer
{
	private SimpleObjectProperty<BitSet> bits;
	private static int noOfSegments = 7;


	public SevenSegementMultiplexer()
	{
		bits = new SimpleObjectProperty<BitSet>(convert(SevenDigit.ZERO.getCode()));
	}


	private static BitSet convert(long value)
	{
		BitSet bits = new BitSet(noOfSegments);
		int index = 0;
		while (value != 0L)
		{
			if (value % 2L != 0)
			{
				bits.set(index);
			}
			++index;
			value = value >>> 1;
		}
		return bits;
	}


	public void set(SevenDigit value)
	{
		bits.set(convert(value.getCode()));
	}


	private int getNumber()
	{
		int value = 0;
		for (int i = 0; i < bits.get().length(); ++i)
		{
			value += bits.get().get(i) ? (1L << i) : 0L;
		}
		return value;
	}


	public SevenDigit getDigit()
	{
		return SevenDigit.findDigit(getNumber());
	}


	public void addListener(InvalidationListener toAdd)
	{
		bits.addListener(toAdd);
	}
}
