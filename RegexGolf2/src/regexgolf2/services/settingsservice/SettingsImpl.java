package regexgolf2.services.settingsservice;

import java.util.Properties;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

class SettingsImpl implements Settings
{
	static final String _SQLITE_DBPATH_PROPERTY = "SQLiteDBPath";
	
	private final Properties _settings;
	
	
	
	/**
	 * Constructs a Settings Object that uses the given Properties Object internally
	 */
	@Requires("settings != null")
	public SettingsImpl(Properties settings)
	{
		_settings = settings;
		
		boolean isValid = validate();
		if (!isValid)
			throw new IllegalArgumentException(
					"The given Properties did not contain all the Settings that are neccessary");
	}
	
	/**
	 * This constructor can be used to initialize every property manually.
	 * This is needed to construct a default Settings Object.
	 */
	@Requires({
		"sQLiteDBPath != null"
	})
	public SettingsImpl(
			String sQLiteDBPath)
	{
		_settings = new Properties();
		
		_settings.setProperty(_SQLITE_DBPATH_PROPERTY, sQLiteDBPath);
		
		boolean isValid = validate();
		if (!isValid)
			throw new IllegalArgumentException(
					"The given Properties did not contain all the Settings that are neccessary");
	}
	
	

	private boolean validate()
	{
		if (getSQLiteDBPath() == null)
			return false;
		
		return true;
	}
	
	/**
	 * Package private method that is used to get the internal Properties Object.
	 * This is needed to persist the Settings to the filesystem.
	 */
	@Ensures("result != null")
	Properties getProperties()
	{
		return _settings;
	}
	
	@Override
	public String getSQLiteDBPath()
	{
		return _settings.getProperty(_SQLITE_DBPATH_PROPERTY);
	}
	
	@Override
	public void setSQLiteDBPath(String path)
	{
		_settings.setProperty(_SQLITE_DBPATH_PROPERTY, path);
	}
}
