package regexgolf2.services.propertyservice;

import com.google.java.contract.Requires;

public class SettingsService
{
	public static final String DEFAULT_PATH = "settings.properties";
	private final String _path;
	
	
	
	
	@Requires("path != null")
	public SettingsService(String path)
	{
		_path = path;
	}
	
	
	
	public void load()
	{
		
	}
	
	public void create()
	{
		
	}
}
