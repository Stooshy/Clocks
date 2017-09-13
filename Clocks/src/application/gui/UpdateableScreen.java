package application.gui;

public class UpdateableScreen implements Updateable
{
	private BaseTimeLine timer;


	public UpdateableScreen(BaseTimeLine provider)
	{
		timer = provider;
	}


	@Override
	public void pauseUpdate()
	{
		timer.pauseUpdate();
	}


	@Override
	public void startUpate()
	{
		timer.startUpate();
	}


	@Override
	public boolean isRunning()
	{
		return timer.isRunning();
	}

}
