package application.gui.segment;

import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Class for handling the segments of a SevenSegment.
 * 
 * @author User
 *
 */
final class SevenSegmentsControl extends Control implements SevenDigitsHandler
{
	private final SevenSegementMultiplexer multiPlexer = new SevenSegementMultiplexer();


	public SevenSegmentsControl(String css)
	{
		getStyleClass().add(css);
		setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				SevenSegmentsControl seg = (SevenSegmentsControl) event.getSource();
				if (event.getButton() == MouseButton.PRIMARY)
					seg.count(true);
				else
					seg.count(false);
			}
		});
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
		set(multiPlexer.getNextNumber());
	}


	public int getValueDisplayed()
	{
		return multiPlexer.getSevenDigit().ordinal();
	}

}
