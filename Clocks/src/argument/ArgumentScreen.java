package argument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import argument.ArgumentInterpreter.Arguments;

public class ArgumentScreen extends Parameter<Integer>
{
	private ArrayList<Integer> parameter;


	public ArgumentScreen()
	{
		super(Arguments.SCREENS);
		parameter = new ArrayList<Integer>();
	}


	@Override
	public Class<Integer> getParameterType()
	{
		return Integer.class;
	}


	@Override
	public int getMaxParameter()
	{
		return argument.maxParameters;
	}


	@Override
	public void addParameter(List<Integer> toAdd)
	{
		parameter.addAll((Collection<? extends java.lang.Integer>) toAdd);
	}


	public List<Integer> getParameters()
	{
		return parameter;
	}
}
