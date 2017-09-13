package application.gui.webcam;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class WebImage extends StackPane
{
	private String url;
	public double maxWidth = 240.0;


	public WebImage(String url)
	{
		this.url = url;
		update();
	}


	public void update()
	{
		try
		{
			getChildren().clear();
			ImageView iv = new ImageView(this.url);
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			double width = iv.getImage().getWidth() >= maxWidth ? maxWidth : iv.getImage().getWidth();
			iv.setFitWidth(width);
			getChildren().add(iv);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
