package application;

public interface Timer
{
	public void set(int minutes, int seconds, int milliSeconds);


	/**
	 * 
	 * @return value between 0 - 99 representing seconds
	 * @see {@link application.gui.segment.SevenDigits}
	 *
	 */
	int getSeconds();


	/**
	 * 
	 * @return value between 0 - 99 representing minutes
	 */
	int getMinutes();


	/**
	 * 
	 * @return value between 0 - 999 representing millisconds
	 */
	int getMilliSeconds();
}
