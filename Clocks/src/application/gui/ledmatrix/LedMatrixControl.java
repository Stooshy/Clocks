package application.gui.ledmatrix;

import application.ObserveableTimeProvider;
import application.gui.Updateable;
import application.gui.screen.ScreenNode;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;

public class LedMatrixControl extends Control implements ScreenNode, Updateable
{
	public static final double PREFERRED_WIDTH = 266;
	public static final double PREFERRED_HEIGHT = 33;
	public static final double MINIMUM_WIDTH = PREFERRED_WIDTH;
	public static final double MINIMUM_HEIGHT = PREFERRED_HEIGHT;
	public static final double MAXIMUM_WIDTH = 1024;
	public static final double MAXIMUM_HEIGHTS = PREFERRED_HEIGHT;
	private LedMatrixMultiplexer multiPlexerM = new LedMatrixMultiplexer();
	private ObserveableTimeProvider provider;
	private StringProperty text;


	public LedMatrixControl(ObserveableTimeProvider provider)
	{
		getStyleClass().add("ledmatrixskin");
		init();
		text = new SimpleStringProperty(this, "text", "");
		this.provider = provider;
		provider.addSecondsChangedListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				consumeTime();
			}
		});
	}


	private void init()
	{
		if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0
				|| Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0)
		{
			if (getPrefWidth() > 0 && getPrefHeight() > 0)
			{
				setPrefSize(getPrefWidth(), getPrefHeight());
			}
			else
			{
				setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
			}
		}

		if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0)
		{
			setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
		}

		if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0)
		{
			setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHTS);
		}
	}


	@Override
	public Node getNode()
	{
		return this;
	}


	private void consumeTime()
	{
		int sec = provider.getSeconds();
		int min = provider.getMinutes();
		int h = provider.getHours();
		multiPlexerM.set(MatrixValues.findDigit((h / 10) % 10), MatrixValues.findDigit(h % 10), MatrixValues.DOULEPOINT,
				MatrixValues.findDigit((min / 10) % 10), MatrixValues.findDigit(min % 10), MatrixValues.DOULEPOINT,
				MatrixValues.findDigit((sec / 10) % 10), MatrixValues.findDigit(sec % 10));
	}


	final void addSecondsChangedListener(InvalidationListener toAdd)
	{
		multiPlexerM.addListener(toAdd);
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


	@Override
	public void pauseUpdate()
	{
		provider.pauseUpdate();
	}


	@Override
	public void startUpate()
	{
		this.provider.startUpate();
	}


	@Override
	public boolean isRunning()
	{
		return provider.isRunning();
	}
	
	@Override
	public void toggleMode()
	{
		if (isRunning())
		{
			pauseUpdate();
		}
		else
		{
			startUpate();
		}
	}
}
