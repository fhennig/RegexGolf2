package regexgolf2.services.settingsservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.google.java.contract.Requires;

/**
 * This class provides an interface to a settings file.
 * It can load and save settings, which are persistent between sessions.
 */
public class SettingsService
{
	public static final String COMMENT = "Settings File to configure RegexGolf\n" +
			"If file syntax is invalid, the file will be replaced with the default settings file";
	public static final File DEFAULT_FILE = new File("settings.properties");
	private final File _file;
	private SettingsImpl _settings;
	
	
	
	@Requires("file != null")
	public SettingsService(File file)
	{
		_file = file;
	}
	
	
	
	/**
	 * Can return null if the Settings are not loaded.
	 */
	public Settings getSettings()
	{
		return _settings;
	}
	
	/**
	 * Calls the load method and returns false if any Exceptions occurred.
	 * True otherwise
	 */
	public boolean tryLoad()
	{
		try { load(); return true; }
		catch (Exception ex) { return false; }
	}
	
	public void load() throws IOException, InvalidFileContentException
	{
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(_file));
		Properties settings = new Properties();
		try
		{
			settings.load(inputStream);
			_settings = new SettingsImpl(settings);
		}
		catch (IllegalArgumentException iae)
		{
			throw new InvalidFileContentException();
		}
	}
	
	public boolean tryCreateDefaultFile()
	{

		try { createDefaultFile(); return true; }
		catch (Exception ex) { return false; }
	}
	
	public void createDefaultFile() throws IOException
	{
		_settings = new DefaultSettingsBuilder().getDefaultSettings();
		if (!_file.exists())
			_file.createNewFile();
		save();
	}
	
	public boolean trySave()
	{

		try { save(); return true; }
		catch (Exception ex) { return false; }
	}
	
	public void save() throws IOException
	{
		OutputStream outStream = new FileOutputStream(_file);
		_settings.getProperties().store(outStream, COMMENT);
	}
}
