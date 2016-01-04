package application.gui.ledmatrix;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

public class LedMatrixMultiplexer
{
	private SimpleObjectProperty<boolean[][]> bits;


	public LedMatrixMultiplexer()
	{
		bits = new SimpleObjectProperty<boolean[][]>(new boolean[8][8]);
	}


	private static boolean[][] convert(byte[] values)
	{
		boolean[][] valueBits = new boolean[8][8];
		int row = 0;
		int col = 0;
		for (int value : values)
		{
			while (value != 0)
			{
				if (value % 2 != 0)
				{
					valueBits[col][row] = true;
				}
				++col;
				value = value >>> 1;
			}
			++row;
			col = 0;
		}
		return valueBits;
	}


	public void set(MatrixValues... values)
	{
		boolean[][] result = new boolean[8][values.length * 8];
		int offset = 0;
		for (MatrixValues value : values)
		{
			boolean[][] first = convert(value.getCode());

			for (int row = 0; row < 8; row++)
			{
				for (int col = 0; col < first[0].length; col++)
				{
					result[row][col + offset] = first[row][col];
				}
			}
			offset = offset + 8;
		}
		bits.set(result);
	}


	public void addListener(InvalidationListener toAdd)
	{
		bits.addListener(toAdd);
	}
}
