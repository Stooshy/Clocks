package application.geodata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataFile
{
	public List<ImageGeoLocation> locations;
	private String filename;

	public DataFile(String filename)
	{
		this.filename = filename;
		locations = new ArrayList<ImageGeoLocation>();
		openFile();
	}


	private void openFile()
	{
		try
		{
			BufferedReader bf = new BufferedReader(new FileReader(filename));
			for (String line; (line = bf.readLine()) != null;)
			{
				locations.add(new ImageGeoLocation(line.split(",")));
			}
			bf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
