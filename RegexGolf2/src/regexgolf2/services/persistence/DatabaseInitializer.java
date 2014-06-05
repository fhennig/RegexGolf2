package regexgolf2.services.persistence;

import java.io.File;
import java.io.IOException;

import com.google.java.contract.Requires;

public class DatabaseInitializer
{
	@Requires("file != null")
	public void createDatabase(File file) throws IOException
	{
		if (!file.exists())
			file.createNewFile();
		
		
	}
}
