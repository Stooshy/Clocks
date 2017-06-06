package application.gui.screen;

import java.util.SortedMap;
import java.util.TreeMap;

import application.TimeScreen;
import application.gui.Updateable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ScreensController
{

	private SortedMap<TimeScreen, Node> screens = new TreeMap<TimeScreen, Node>();
	private ObjectProperty<TimeScreen> actScreen;
	private final StackPane sp;


	public ScreensController(TimeScreen... screens)
	{
		sp = new StackPane();
		for (TimeScreen screen : screens)
			addScreen(screen);
	}


	public Node getScreenNode(TimeScreen screen)
	{
		for (TimeScreen key : screens.keySet())
		{
			if (key.equals(screen))
				return key.getNode();
		}
		throw new IllegalArgumentException("screen not found");
	}


	public void addScreen(TimeScreen screenId)
	{
		screens.put(screenId, screenId.getNode());
		actScreen = new SimpleObjectProperty<TimeScreen>();
	}


	public void setNextScreen()
	{
		((Updateable) getScreen().screen).pauseUpdate();
		setScreen(actScreen.get().next());
		if (getScreen() == TimeScreen.COUNTER_SCREEN)
		{
			return;
		}
		((Updateable) getScreen().screen).startUpate();
	}


	public boolean setScreen(final TimeScreen key)
	{
		if (screens.get(key) != null)
		{ // screen loaded
			final DoubleProperty opacity = sp.opacityProperty();

			if (!sp.getChildren().isEmpty())
			{
				Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
						new KeyFrame(new Duration(500),

								new EventHandler<ActionEvent>()
								{
									public void handle(ActionEvent event)
									{
										sp.getChildren().clear();
										sp.getChildren().add(screens.get(key));
										Timeline fadeIn = new Timeline(
												new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
												new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
										fadeIn.play();

									}
								}, new KeyValue(opacity, 0.0)));
				fade.play();
			}
			else
			{
				// no one else been displayed, then just show
				sp.getChildren().add(screens.get(key));
			}
			actScreen.set(key);
			return true;
		}
		else
		{
			System.out.println("screen hasn't been loaded!\n");
			return false;
		}
	}


	public TimeScreen getScreen()
	{
		return actScreen.get();
	}


	public void registerScreenChangedListener(ChangeListener<TimeScreen> listener)
	{
		actScreen.addListener(listener);
	}


	public double getPrefHeight()
	{
		return screens.get(getScreen()).prefHeight(0);
	}


	public double getPrefWidth()
	{
		return screens.get(getScreen()).prefWidth(0);
	}


	public Node getActualScreen()
	{
		return sp;
	}


	public double getMaxHeight()
	{
		return screens.get(getScreen()).maxHeight(0);
	}


	public double getMaxWidth()
	{
		return screens.get(getScreen()).maxWidth(0);
	}


	public Node[] getScreens()
	{
		return screens.values().toArray(new Node[screens.values().size()]);
	}

}
