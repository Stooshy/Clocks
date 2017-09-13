package application.counter;

public interface Counter
{
	public void set(int minutes, int seconds, int milliSeconds);


	public void setCountMode(CounterMode newMode);
}
