package application.gui.ledmatrix;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

public class LedMatrixMultiplexer
{
	private SimpleObjectProperty<boolean[][]> bits;


	public LedMatrixMultiplexer()
	{
		bits = new SimpleObjectProperty<boolean[][]>(new boolean[16][16]);
	}


	private static boolean[][] convert(int[] values)
	{
		boolean[][] valueBits = new boolean[16][16];
		int row = 0;
		int col = 0;
		for (int value : values)
		{
			while (value != 0)
			{
				if (value % 2 != 0)
				{
					valueBits[row][col] = true;
				}
				++col;
				value = value >>> 1;
			}
			++row;
			col = 0;
		}
		return valueBits;
	}


	public void set(MatrixValues value)
	{
		bits.set(convert(value.getCode()));
	}


	public void addListener(InvalidationListener toAdd)
	{
		bits.addListener(toAdd);
	}
}
