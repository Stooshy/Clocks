package application.gui.segment;

import javafx.beans.InvalidationListener;
import javafx.scene.control.Control;

/**
 * Class for handling the segments of a SevenSegment.
 * 
 * @author User
 *
 */
public final class SevenSegmentsControl extends Control implements SevenDigitsHandler
{
	private final SevenSegementMultiplexer multiPlexer = new SevenSegementMultiplexer();


	public SevenSegmentsControl(String css)
	{
		getStyleClass().add(css);
	}


	public void addNewValueListener(InvalidationListener toAdd)
	{
		multiPlexer.addListener(toAdd);
		set(SevenDigit.ZERO);
	}


	public void set(SevenDigit value)
	{
		multiPlexer.set(value);
	}


	public void count(boolean up)
	{
		set(multiPlexer.getDigit().nextNumber(up));
	}


	public int getValue()
	{
		return multiPlexer.getDigit().ordinal();
	}

}
