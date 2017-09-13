package application.gui.buttons.listeners;

import application.TimeScreen;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TimeScreenPref extends TimeScreenSizeChanged
{

	public TimeScreenPref(Stage stage)
	{
		super(stage);
	}


	@Override
	public void changed(ObservableValue<? extends TimeScreen> observable, TimeScreen oldValue, TimeScreen newValue)
	{
		double buttonHeight = ((BorderPane) stage.getScene().getRoot()).topProperty().get().getBoundsInParent()
				.getHeight();
		Node node = newValue.getNode();
		stage.setHeight(node.prefHeight(0) + buttonHeight);
		stage.setWidth(node.prefWidth(0));
	}
}