package application.gui.segment;

public interface SevenDigitsHandler
{

	/**
	 * @param value
	 *            new value
	 */
	public void set(SevenDigit value);


//	/**
//	 * @param up
//	 *            true: 0-9, false 9-0
//	 */
//	public void count(boolean up);


	/**
	 * @return Displayed value
	 */
	public int getDisplayedValue();
	
	/**
	 * Listen for mouseclicks.
	 */
	public void setEditable();
}
