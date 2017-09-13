package application.gui.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.TimeScreen;
import application.gui.Updateable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ScreensController
{
	private List<TimeScreen> screens = new ArrayList<TimeScreen>();
	private ObjectProperty<TimeScreen> actScreen;
	private final StackPane sp;


	public ScreensController(TimeScreen... screens)
	{
		sp = new StackPane();
		for (TimeScreen screen : screens)
			addScreen(screen);

		actScreen = new SimpleObjectProperty<TimeScreen>();
		actScreen.addListener(new ChangeListener<TimeScreen>()
		{
			@Override
			public void changed(ObservableValue<? extends TimeScreen> observable, TimeScreen oldValue,
					TimeScreen newValue)
			{
				if (oldValue != null)
					if (oldValue != TimeScreen.COUNTER_SCREEN)
						((Updateable) oldValue.screen).pauseUpdate();
			}
		});
	}


	public void addScreen(TimeScreen screenId)
	{
		screens.add(screenId);
	}


	public void setNextScreen()
	{
		if (screens.size() == 1)
			return;

		int idx = screens.indexOf(actScreen.get());
		if (idx > -1)
		{
			idx = (idx + 1) % screens.size();
			setScreen(screens.get(idx));
		}
	}


	public boolean setScreen(final TimeScreen key)
	{
		if (screens.contains(key))
		{
			final DoubleProperty opacity = sp.opacityProperty();

			Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
					new KeyFrame(new Duration(500),

							new EventHandler<ActionEvent>()
							{
								public void handle(ActionEvent event)
								{
									sp.getChildren().clear();
									sp.getChildren().add(getSreen(key));
									Timeline fadeIn = new Timeline(
											new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
											new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
									fadeIn.play();

								}
							}, new KeyValue(opacity, 0.0)));
			fade.setOnFinished(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent event)
				{
					if (actScreen.get() != TimeScreen.COUNTER_SCREEN)
						((Updateable) actScreen.get().screen).startUpate();
				}
			});
			fade.play();
			actScreen.set(key);
			return true;
		}
		else
		{
			System.out.println("Screen not found: " + key.ordinal());
			return false;
		}
	}


	public boolean isActualScreen(TimeScreen toCompare)
	{
		return getActualScreen() == toCompare;
	}


	protected TimeScreen getActualScreen()
	{
		return actScreen.get();
	}


	public void registerScreenChangedListener(ChangeListener<TimeScreen> listener)
	{
		actScreen.addListener(listener);
	}


	public double getPrefHeight()
	{
		return getActualScreen().getNode().prefHeight(0);
	}


	public double getPrefWidth()
	{
		return getActualScreen().getNode().prefWidth(0);
	}


	public double getMaxHeight()
	{
		return getActualScreen().getNode().maxHeight(0);
	}


	public double getMaxWidth()
	{
		return getActualScreen().getNode().maxWidth(0);
	}


	public Node getPane()
	{
		return sp;
	}


	public Node[] getScreens()
	{
		return screens.stream().map(TimeScreen::getNode).collect(Collectors.toList()).toArray(new Node[screens.size()]);
	}


	private Node getSreen(Object key)
	{
		return screens.get((screens.indexOf(key))).getNode();
	}
	
	public void setFirstScreen()
	{
		setScreen(screens.get(0));
	}
}
