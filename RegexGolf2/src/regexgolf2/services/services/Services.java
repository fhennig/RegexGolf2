package regexgolf2.services.services;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.persistence.database.Database;
import regexgolf2.services.persistence.database.DatabaseInitializer;
import regexgolf2.services.persistence.mappers.Mappers;
import regexgolf2.services.settingsservice.SettingsService;

import com.google.java.contract.Ensures;

public class Services
{
	private static final Logger _LOG = Logger.getLogger(Services.class.getName());

	private SettingsService _settingsService;
	private PersistenceService _persistenceService;
	private ChallengeGeneratorService _generator;



	public Services() throws InitializingException
	{
		load();
	}



	/**
	 * @throws InitializingException
	 *             if some module could not be initialized
	 */
	private void load() throws InitializingException
	{
		try
		{
			if (!initSettingsService())
				throw new InitializingException();
			_LOG.info("SettingsService initialized");

			if (!initPersistenceService(_settingsService.getSettings().getSQLiteDBPath()))
				throw new InitializingException();
			_LOG.info("PersistenceService initialized");

			if (!initChallengeGeneratorService())
				throw new InitializingException();
			_LOG.info("ChallengeGenerator initialized");
		} catch (InitializingException e)
		{
			throw e;
		} catch (Exception ex)
		{
			_LOG.severe(ex.toString());
			// TODO use error handler here
			JOptionPane.showMessageDialog(null, "Unexpected fatal Error!\n\n" + ex.toString());
			ex.printStackTrace();
			throw new InitializingException();
		}
	}

	private boolean initSettingsService()
	{
		_settingsService = new SettingsService(SettingsService.DEFAULT_FILE);
		boolean settingsLoaded = _settingsService.tryLoad();
		if (settingsLoaded)
			return true;

		boolean settingsCreated = _settingsService.tryCreateDefaultFile();
		if (settingsCreated)
			return true;

		JOptionPane.showMessageDialog(null, "Could not find or create a settings.properties File!");
		return false;
	}

	@Ensures("result == (_persistenceService != null)")
	private boolean initPersistenceService(String dbPath)
	{
		File dbFile = new File(dbPath);
		boolean dbIsNew = false;
		if (!dbFile.exists())
			try
			{
				dbFile.createNewFile();
				dbIsNew = true;
			} catch (IOException e)
			{
				JOptionPane.showMessageDialog(null,
						"Could not find or create a Database File at:\n" + dbPath);
				return false;
			}
		;
		Database db = null;
		try
		{
			db = new Database(dbFile);
		} catch (ClassNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load the Database driver!");
			return false;
		} catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Could not connect to the Database!");
			return false;
		}

		if (dbIsNew)
		{
			DatabaseInitializer dbInit = new DatabaseInitializer(db);
			try
			{
				dbInit.createTablesIfNotExists();
				dbInit.insertDefaultWords();
			} catch (SQLException e)
			{
				JOptionPane.showMessageDialog(null, "Could not initialize the Database!");
				return false;
			}
		}
		
		try
		{
			_persistenceService = new PersistenceService(new Mappers(db));
		} catch (PersistenceException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load data from the Database!");
			return false;
		}
		return true;
	}

	private boolean initChallengeGeneratorService()
	{
		_generator = new ChallengeGeneratorService(_persistenceService.getWordRepository());
		return true;
	}

	@Ensures("result != null")
	public PersistenceService getPersistenceService()
	{
		return _persistenceService;
	}

	@Ensures("result != null")
	public ChallengeGeneratorService getGeneratorService()
	{
		return _generator;
	}
}
