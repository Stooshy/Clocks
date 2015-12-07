package application.gui.segment;

import javafx.beans.InvalidationListener;
import javafx.scene.control.Control;

public final class SevenSegmentsControl extends Control
{
	private final SevenSegementMultiplexer multiPlexer = new SevenSegementMultiplexer();


	public SevenSegmentsControl()
	{
		getStyleClass().add("segment");
	}


	public void addNewValueListener(InvalidationListener toAdd)
	{
		multiPlexer.getBits().addListener(toAdd);
	}


	public void set(SevenDigit value)
	{
		multiPlexer.set(value);
	}


	public SevenDigit getDigit()
	{
		return multiPlexer.getDigit();
	}
}
