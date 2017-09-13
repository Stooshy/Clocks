package argument.parameter;

public class ParameterFactory
{

	@SuppressWarnings("unchecked")
	public static <T> T getForType(Class<T> type, String value)
	{
		if (type == Integer.class)
		{
			return (T) new Integer(value);
		}

		return (T) value;
	}
}
