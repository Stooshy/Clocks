package application.gui;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public abstract class BaseTimeLine
{
	private Timeline infoLine;


	public BaseTimeLine(Duration update)
	{
		infoLine = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				consume();
			}
		}), new KeyFrame(update));
		infoLine.setCycleCount(Animation.INDEFINITE);
	}


	public void startUpate()
	{
		infoLine.play();
	}


	protected abstract void consume();


	public void pauseUpdate()
	{
		infoLine.pause();
	}


	public boolean isRunning()
	{
		return infoLine.getStatus() == Status.RUNNING;
	}

}
