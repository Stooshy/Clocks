package argument;

import java.util.List;

import argument.ArgumentInterpreter.Arguments;

public abstract class Parameter<T>
{
	public Arguments argument;


	public Parameter(Arguments name)
	{
		argument = name;
	}


	public abstract void addParameter(List<T> toAdd);


	public abstract List<T> getParameters();


	public abstract Class<?> getParameterType();


	public abstract int getMaxParameter();

}
