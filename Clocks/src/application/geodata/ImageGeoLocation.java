package application.geodata;

public class ImageGeoLocation
{
	public float latitude;
	public float longitude;
	public String imageUrl;


	public ImageGeoLocation()
	{
	}


	public ImageGeoLocation(String[] entry) throws NumberFormatException
	{
		if (entry.length != 3)
			return;

		latitude = Float.parseFloat(entry[0]);
		longitude = Float.parseFloat(entry[1]);
		imageUrl = entry[2].trim();
	}

}
