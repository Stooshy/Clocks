package application.gui.buttons.listeners;

import application.TimeScreen;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TimeScreenMax extends TimeScreenSizeChanged
{

	public TimeScreenMax(Stage stage)
	{
		super(stage);
	}


	@Override
	public void changed(ObservableValue<? extends TimeScreen> observable, TimeScreen oldValue, TimeScreen newValue)
	{
		double infoWidth = ((BorderPane) stage.getScene().getRoot()).rightProperty().get().getBoundsInLocal()
				.getWidth();
		Node node = newValue.getNode();
		stage.setWidth(node.maxWidth(0) + infoWidth);
		stage.setHeight(node.maxHeight(0));
	}
}