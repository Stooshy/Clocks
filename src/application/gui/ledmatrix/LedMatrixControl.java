package application.gui.ledmatrix;

import application.gui.ScreenNode;
import application.gui.TimeProvider;
import application.gui.segment.TimeConsumer;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;

public class LedMatrixControl extends Control implements TimeConsumer, ScreenNode
{
	private LedMatrixMultiplexer multiPlexerM = new LedMatrixMultiplexer();
	private TimeProvider timeProvider;
	private StringProperty text;


	public LedMatrixControl(TimeProvider provider)
	{
		getStyleClass().add("ledmatrixskin");
		setTimeProvider(provider);
		text = new SimpleStringProperty(this, "text", "");
		setPrefHeight(90);
		setPrefWidth(265);
	}


	@Override
	public Node getNode()
	{
		return this;
	}


	@Override
	public void consumeTime()
	{
		int sec = timeProvider.getSeconds();
		int min = timeProvider.getMinutes();
		int h = timeProvider.getHours();
		multiPlexerM.set(MatrixValues.findDigit((h / 10) % 10), MatrixValues.findDigit(h % 10), MatrixValues.DOULEPOINT,
				MatrixValues.findDigit((min / 10) % 10), MatrixValues.findDigit(min % 10), MatrixValues.DOULEPOINT,
				MatrixValues.findDigit((sec / 10) % 10), MatrixValues.findDigit(sec % 10));
	}


	@Override
	public void setTimeProvider(TimeProvider provider)
	{
		timeProvider = provider;
	}


	public void addMinutesListener(InvalidationListener toAdd)
	{
		multiPlexerM.addListener(toAdd);
		consumeTime();
	}


	public String getSkinText()
	{
		return text.get();
	}


	public void setSkinText(String value)
	{
		text.set(value);
	}


	public final void addTextListener(InvalidationListener toAdd)
	{
		text.addListener(toAdd);
	}


	public void addClickedListener(EventHandler<MouseEvent> toAdd)
	{
		setOnMouseClicked(toAdd);
	}

}
