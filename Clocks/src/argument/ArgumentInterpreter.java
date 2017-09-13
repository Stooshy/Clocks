package argument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import argument.parameter.ParameterFactory;

public class ArgumentInterpreter
{
	private Map<Arguments, Parameter<?>> arguments;

	public enum Arguments
	{
		MAX("-max", 0), SCREENS("-screens", 4);

		String name;
		int maxParameters;


		Arguments(String value1, int value2)
		{
			name = value1;
			maxParameters = value2;
		}
	}


	public ArgumentInterpreter(List<String> list)
	{
		arguments = new HashMap<Arguments, Parameter<?>>();
		for (Arguments ARGUMENT : Arguments.values())
		{
			if (!list.contains(ARGUMENT.name))
				continue;

			switch (ARGUMENT)
			{
			case MAX:
				arguments.put(ARGUMENT, new ArgumentSize());
				break;
			case SCREENS:
				Parameter<Integer> arg = new ArgumentScreen();
				List<Integer> parameters = getParametersFor(arg, list);
				arg.addParameter(parameters);
				arguments.put(ARGUMENT, arg);
				break;
			default:
				break;
			}
		}
	}


	public SreenSize getScreenSize()
	{
		return arguments.containsKey(Arguments.MAX) ? SreenSize.MAX : SreenSize.PREF;
	}


	public Parameter<?> get(Arguments ARG)
	{
		if (arguments.containsKey(ARG))
			return arguments.get(ARG);
		return null;
	}


	@SuppressWarnings("unchecked")
	private <T> List<T> getParametersFor(Parameter<T> args, List<String> list)
	{
		ArrayList<T> result = new ArrayList<T>();
		if (args.getMaxParameter() == 0)
			return result;

		int index = list.indexOf(args.argument.name);
		if (list.size() <= index + 1)
		{
			throw new IllegalArgumentException("No parameters found:" + args.argument.name);
		}
		String[] values = list.get(index + 1).split(",");
		for (int idx = 0; idx < args.getMaxParameter(); idx++)
		{
			if (idx < values.length)
				result.add((T) ParameterFactory.getForType(args.getParameterType(), values[idx]));
		}
		return result;
	}
}
