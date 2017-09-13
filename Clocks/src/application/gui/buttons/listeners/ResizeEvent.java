package application.gui.buttons.listeners;

import application.gui.screen.ScreensController;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class ResizeEvent extends ScreenControlEvent
{
	private final Stage stage;


	public ResizeEvent(ScreensController controller, Stage stage)
	{
		super(controller);
		this.stage = stage;
	}


	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
		if (newValue)
		{
			stage.setHeight(controller.getMaxHeight()
					+ ((BorderPane) stage.getScene().getRoot()).topProperty().get().getBoundsInLocal().getHeight());
			stage.setWidth(controller.getMaxWidth());
		}
		else
		{
			stage.setHeight(controller.getPrefHeight()
					+ ((BorderPane) stage.getScene().getRoot()).topProperty().get().getBoundsInLocal().getHeight());
			stage.setWidth(controller.getPrefWidth());
		}
	}
}