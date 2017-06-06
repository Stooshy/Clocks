package application.gui.util;

import javafx.concurrent.Task;
import javafx.util.Duration;

public class ShowProgressThread extends Task<Void>
{
	final private long cycle = 600L;
	double counter;


	public void startUpdateThread(Duration duration)
	{
		counter = cycle / 1000d / duration.toSeconds();
		new Thread(this).start();
	}


	private void update()
	{
		try
		{
			for (double d = 0.0d; d <= 1.0; d += counter)
			{
				Thread.sleep(cycle);
				updateProgress(d, 1.0);
			}
			updateProgress(1.0, 1.0);
		}
		catch (InterruptedException e)
		{
			Thread.interrupted();
		}
	}


	@Override
	protected Void call() throws Exception
	{
		update();
		return null;
	}
}
