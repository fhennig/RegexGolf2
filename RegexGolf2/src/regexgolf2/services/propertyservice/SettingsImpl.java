package regexgolf2.services.propertyservice;

import java.util.Properties;

class SettingsImpl implements Settings
{
	private final Properties _settings;
	
	
	
	public SettingsImpl(Properties settings)
	{
		_settings = settings;
		
	}
	
	

	@Override
	public String getSQLiteDBPath()
	{
		return _settings.getProperty("SQLiteDBPath");
	}
}
