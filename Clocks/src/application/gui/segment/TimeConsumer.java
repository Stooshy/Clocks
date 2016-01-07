package application.gui.segment;

import application.gui.TimeProvider;

public interface TimeConsumer
{
	public void consumeTime();


	public void setTimeProvider(TimeProvider provider);

}
