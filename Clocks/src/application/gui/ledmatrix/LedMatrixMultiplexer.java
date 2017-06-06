package application.gui.ledmatrix;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;

class LedMatrixMultiplexer
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
					valueBits[row][col] = true;
				}
				++row;
				value = value >>> 1;
			}
			++col;
			row = 0;
		}
		return valueBits;
	}


	public void set(MatrixValues... values)
	{
		boolean[][] result = new boolean[8][values.length * 8];
		int offset = 0;
		int mappedRow = 7;
		for (MatrixValues value : values)
		{
			boolean[][] matrixCode = convert(value.getCode());

			for (int row = 0; row < 8; row++)
			{
				for (int col = 0; col < matrixCode[0].length; col++)
				{
					result[mappedRow][col + offset] = matrixCode[row][col];
				}
				mappedRow--;
			}
			mappedRow = 7;
			offset = offset + 8;
		}
		bits.set(result);
	}


	public void addListener(InvalidationListener toAdd)
	{
		bits.addListener(toAdd);
	}
}
