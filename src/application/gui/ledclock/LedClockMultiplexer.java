package application.gui.ledclock;

import java.util.BitSet;

import javafx.beans.property.SimpleObjectProperty;

public class LedClockMultiplexer
{
	private final SimpleObjectProperty<BitSet> bits;
	private final int noOfSegments;


	public LedClockMultiplexer(int noOfSegments)
	{
		this.noOfSegments = noOfSegments;
		bits = new SimpleObjectProperty<BitSet>(convert(0));
	}


	private BitSet convert(long value)
	{
		BitSet bits = new BitSet(noOfSegments);
		for (int idx = 0; idx < (value % noOfSegments); idx++)
		{
			bits.set(idx);
		}
		return bits;
	}


	public void set(int value)
	{
		getBitsProperty().set(convert(value));
	}


	public int getNumber()
	{
		int value = 0;
		for (int i = 0; i < getBitsProperty().get().length(); ++i)
		{
			value += getBitsProperty().get().get(i) ? (1L << i) : 0L;
		}
		return value;
	}


	public SimpleObjectProperty<BitSet> getBitsProperty()
	{
		return bits;
	}

}
