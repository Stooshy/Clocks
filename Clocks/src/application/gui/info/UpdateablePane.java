package application.gui.info;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public abstract class UpdateablePane extends GridPane
{
	public UpdateablePane(Duration update)
	{
		super();
		startUpate(update);
	}


	public abstract void update(Duration update);


	private void startUpate(final Duration update)
	{
		final Timeline infoLine = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				update(update);
			}
		}), new KeyFrame(update));
		infoLine.setCycleCount(Animation.INDEFINITE);
		infoLine.play();
	}
}
