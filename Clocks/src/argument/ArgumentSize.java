package argument;

import java.util.List;

import argument.ArgumentInterpreter.Arguments;

public class ArgumentSize extends Parameter<String>
{
	public ArgumentSize()
	{
		super(Arguments.MAX);
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
	public void addParameter(List<String> toAdd)
	{
		throw new UnsupportedOperationException();
	}


	public List<String> getParameters()
	{
		throw new UnsupportedOperationException();
	}
}
