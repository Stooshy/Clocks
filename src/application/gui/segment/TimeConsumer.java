package application.gui.segment;

import application.gui.TimeProvider;
import javafx.scene.layout.Pane;

public interface TimeConsumer
{
	public void consumeTime();
	
	public void setTimeProvider(TimeProvider provider);
	
	public void setTime(int seconds, int minutes, int hours, int milli);

	public Pane getPane();
}
