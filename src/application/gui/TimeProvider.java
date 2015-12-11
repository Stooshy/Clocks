package application.gui;

public interface TimeProvider
{
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
	 * @return value between 0 - 99 representing hours
	 */
	int getHours();


	int getMilliseconds();

}
