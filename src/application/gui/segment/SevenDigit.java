package application.gui.segment;

//@formatter:off
/**
 *    x             a   b   c   d   e   f   g
0 	0×3F 	0×7E 	on 	on 	on 	on 	on 	on 	off
1 	0×06 	0×30 	off	on 	on 	off off	off off
2 	0×5B 	0×6D 	on 	on 	off on 	on 	off on
3 	0×4F 	0×79 	on 	on 	on 	on 	off off on
4 	0×66 	0×33 	off	on 	on 	off off on 	on
5 	0×6D 	0×5B 	on 	off on 	on 	off on 	on
6 	0×7D 	0×5F 	on 	off on 	on 	on 	on 	on
7 	0×07 	0×70 	on 	on 	on 	off off off off
8 	0×7F 	0×7F 	on 	on 	on 	on 	on 	on 	on
9 	0×6F 	0×7B 	on 	on 	on 	on 	off on 	on
A 	0×77 	0×77 	on 	on 	on 	off on 	on 	on
b 	0×7C 	0×1F 	off off on 	on 	on 	on 	on
C 	0×39 	0×4E 	on 	off off on 	on 	on 	off
d 	0×5E 	0×3D 	off on 	on 	on 	on 	off on
E 	0×79 	0×4F 	on 	off off on 	on 	on 	on
F 	0×71 	0×47 	on 	off off off on 	on 	on
 */
//@formatter:on

public enum SevenDigit
{
	ZERO(63), ONE(6), TWO(91), THREE(79), FOUR(102), FIVE(109), SIX(125), SEVEN(39), EIGHT(127), NINE(111), A(119), B(
			124), C(57), D(94), E(121), F(113);

	private int code;


	private SevenDigit(int theCode)
	{
		code = theCode;
	}


	public int getCode()
	{
		return code;
	}


	public static SevenDigit findDigit(int code)
	{
		for (SevenDigit num : values())
			if (num.getCode() == code)
				return num;

		throw new IllegalArgumentException("Code not valid.");
	}


	/**
	 * 0-9
	 */
	public SevenDigit nextNumber(boolean up)
	{
		if (up)
		{
			if (values().length > (ordinal() + 1) && (ordinal() + 1) < 10)
				return values()[(ordinal() + 1) % values().length];
		}
		else
		{
			if (values().length > (ordinal() - 1) && (ordinal() - 1) >= 0)
				return values()[(ordinal() - 1) % values().length];
		}

		return SevenDigit.ZERO;

	}


	/**
	 * A-F
	 */
	public SevenDigit nextLetter()
	{
		if (values().length > (ordinal() + 1) && (ordinal() + 1) > 9)
			return values()[(ordinal() + 1) % values().length];
		return SevenDigit.A;
	}
}
