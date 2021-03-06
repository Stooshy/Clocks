package application.gui.ledmatrix;

public enum MatrixValues
{
	SPACE(new byte[]
	{
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
	}, 32), EXMARK(new byte[]
	{
			0x00, 0x00, 0x00, 0x7D, 0x00, 0x00, 0x00, 0x00
	}, 33), POINT(new byte[]
	{
			0x01, 0x0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
	}, 46), ZERO(new byte[]
	{
			0x00, 0x3E, 0x41, 0x41, 0x41, 0x3E, 0x00, 0x00
	}, 48), ONE(new byte[]
	{
			0x00, 0x08, 0x10, 0x20, 0x7F, 0x00, 0x00, 0x00
	}, 49), TWO(new byte[]
	{
			0x00, 0x21, 0x43, 0x45, 0x49, 0x31, 0x00, 0x00
	}, 50), THREE(new byte[]
	{
			0x00, 0x22, 0x41, 0x49, 0x49, 0x36, 0x00, 0x00
	}, 51), FOUR(new byte[]
	{
			0x00, 0x0C, 0x14, 0x24, 0x7F, 0x04, 0x00, 0x00
	}, 52), FIVE(new byte[]
	{
			0x00, 0x7A, 0x49, 0x49, 0x49, 0x46, 0x00, 0x00
	}, 53), SIX(new byte[]
	{
			0x00, 0x1E, 0x25, 0x49, 0x49, 0x06, 0x00, 0x00
	}, 54), SEVEN(new byte[]
	{
			0x00, 0x40, 0x47, 0x48, 0x50, 0x60, 0x00, 0x00
	}, 55), EIGHT(new byte[]
	{
			0x00, 0x36, 0x49, 0x49, 0x49, 0x36, 0x00, 0x00
	}, 56), NINE(new byte[]
	{
			0x00, 0x32, 0x49, 0x49, 0x49, 0x3E, 0x00, 0x00
	}, 57), DOULEPOINT(new byte[]
	{
			0x00, 0x00, 0x00, 0x12, 0x00, 0x00, 0x00, 0x00
	}, 58), QUESTION(new byte[]
	{
			0x00, 0x20, 0x40, 0x45, 0x48, 0x30, 0x00, 0x00
	}, 63), A(new byte[]
	{
			0x00, 0x3F, 0x44, 0x44, 0x44, 0x3F, 0x00, 0x00
	}, 65), B(new byte[]
	{
			0x00, 0x7F, 0x49, 0x49, 0x49, 0x36, 0x00, 0x00
	}, 66), C(new byte[]
	{
			0x00, 0x3E, 0x41, 0x41, 0x41, 0x22, 0x00, 0x00
	}, 67), D(new byte[]
	{
			0x00, 0x7F, 0x41, 0x41, 0x41, 0x3E, 0x00, 0x00
	}, 68), E(new byte[]
	{
			0x00, 0x7F, 0x49, 0x49, 0x49, 0x41, 0x00, 0x00
	}, 69), F(new byte[]
	{
			0x00, 0x7F, 0x48, 0x48, 0x48, 0x40, 0x00, 0x00
	}, 70), G(new byte[]
	{
			0x00, 0x3E, 0x41, 0x49, 0x49, 0x26, 0x00, 0x00
	}, 71), H(new byte[]
	{
			0x00, 0x7F, 0x08, 0x08, 0x08, 0x7F, 0x00, 0x00
	}, 72), I(new byte[]
	{
			0x00, 0x00, 0x41, 0x7F, 0x41, 0x00, 0x00, 0x00
	}, 73), J(new byte[]
	{
			0x00, 0x02, 0x01, 0x01, 0x7E, 0x00, 0x00, 0x0
	}, 74), K(new byte[]
	{
			0x00, 0x7F, 0x08, 0x14, 0x22, 0x41, 0x00, 0x00
	}, 75), L(new byte[]
	{
			0x00, 0x7F, 0x01, 0x01, 0x01, 0x01, 0x00, 0x00
	}, 76), M(new byte[]
	{
			0x00, 0x7F, 0x20, 0x10, 0x20, 0x7F, 0x00, 0x00
	}, 77), N(new byte[]
	{
			0x00, 0x7F, 0x30, 0x08, 0x06, 0x7F, 0x00, 0x00
	}, 78), O(new byte[]
	{
			0x00, 0x3E, 0x41, 0x41, 0x41, 0x3E, 0x00, 0x00
	}, 79), P(new byte[]
	{
			0x00, 0x7F, 0x48, 0x48, 0x48, 0x30, 0x00, 0x00
	}, 80), Q(new byte[]
	{
			0x00, 0x3E, 0x41, 0x45, 0x43, 0x3F, 0x00, 0x00
	}, 81), R(new byte[]
	{
			0x00, 0x7F, 0x48, 0x4C, 0x4A, 0x31, 0x00, 0x0
	}, 82), S(new byte[]
	{
			0x00, 0x32, 0x49, 0x49, 0x49, 0x26, 0x00, 0x00
	}, 83), T(new byte[]
	{
			0x00, 0x40, 0x40, 0x7F, 0x40, 0x40, 0x00, 0x00
	}, 84), U(new byte[]
	{
			0x00, 0x7E, 0x01, 0x01, 0x01, 0x7E, 0x00, 0x00
	}, 85), V(new byte[]
	{
			0x00, 0x7C, 0x02, 0x01, 0x02, 0x7C, 0x00, 0x00
	}, 86), W(new byte[]
	{
			0x00, 0x7F, 0x02, 0x1C, 0x02, 0x7F, 0x00, 0x00
	}, 87), X(new byte[]
	{
			0x00, 0x63, 0x14, 0x08, 0x14, 0x63, 0x00, 0x00
	}, 88), Y(new byte[]
	{
			0x00, 0x60, 0x10, 0x0F, 0x10, 0x60, 0x00, 0x00
	}, 89), Z(new byte[]
	{
			0x00, 0x43, 0x45, 0x49, 0x51, 0x61, 0x00, 0x00
	}, 90);

	private byte[] code = new byte[8];

	private int charCode;


	private MatrixValues(byte[] matrixCode, int charCode)
	{
		code = matrixCode;
		this.charCode = charCode;
	}


	public byte[] getCode()
	{
		return code;
	}


	public int getCharCode()
	{
		return charCode;
	}


	public static MatrixValues findDigit(int code)
	{
		for (MatrixValues num : values())
			if (num.ordinal() == code + 3)
				return num;

		throw new IllegalArgumentException("Code not found.");
	}


	public static MatrixValues findValue(int charCode)
	{
		for (MatrixValues num : values())
			if (num.getCharCode() == charCode)
				return num;

		throw new IllegalArgumentException("Code not found.");
	}
}
