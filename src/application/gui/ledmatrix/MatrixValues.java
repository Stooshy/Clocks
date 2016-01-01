package application.gui.ledmatrix;

public enum MatrixValues
{
	ZERO(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), ONE(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), TWO(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), THREE(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), FOUR(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), FIVE(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), SIX(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), SEVEN(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), EIGHT(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), NINE(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), A(new int[]
	{
			384, 384, 576, 576, 1056, 1056, 2064, 2064, 4104, 8184, 16380, 8196, 16386, 16386, 32769, 32769
	}), B(new int[]
	{
			384, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), C(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), D(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), E(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	}), F(new int[]
	{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	});

	private int[] code = new int[16];


	private MatrixValues(int[] theCode)
	{
		code = theCode;
	}


	public int[] getCode()
	{
		return code;
	}
}
