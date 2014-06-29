package regexgolf2.services.initializing;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.google.java.contract.Ensures;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.persistence.Database;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.WordMapper;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.services.settingsservice.SettingsService;
import regexgolf2.startup.ChallengeFactory;

public class InitializingService
{
	private static final Logger _LOG = Logger.getLogger(InitializingService.class.getName());
	
	private SettingsService _settingsService;
	private PersistenceService _persistenceService;
	private ChallengeRepository _challengeRepository;
	private WordRepository _wordRepository;
	private ChallengeGeneratorService _generator;
	
	
	
	/**
	 * @return  a ServiceContainer containing all the initialized Services; null if init failed
	 */
	public ServiceContainer start()
	{
		try
		{
			if (!initSettingsService())
				return null;
			_LOG.info("SettingsService initialized");
			
			if (!initPersistenceService(_settingsService.getSettings().getSQLiteDBPath()))
				return null;
			_LOG.info("PersistenceService initialized");
			
			if (!testDB(_persistenceService))
				return null;
			_LOG.info("Database Test successful");
			
			if (!initChallengeRepository(_persistenceService.getSolvableChallengeMapper()))
				return null;
			_LOG.info("ChallengeRepository initialized");
			
			if (!initWordRepository(_persistenceService.getWordMapper()))
				return null;
			_LOG.info("WordRepository initialized");
			
			if (!initChallengeGeneratorService())
				return null;
			_LOG.info("ChallengeGenerator initialized");
			
			return createServiceContainer();
		}
		catch (Exception ex)
		{
			_LOG.severe(ex.toString());
			JOptionPane.showMessageDialog(null, "Unexpected fatal Error!\n\n" + ex.toString());
			ex.printStackTrace();
			return null;
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
		if (!dbFile.exists())
			try
			{
				dbFile.createNewFile();
			} catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Could not find or create a Database File at:\n"+
						dbPath);
				return false;
			};
		Database db = null;
		try
		{
			db = new Database(dbFile);
		}
		catch (ClassNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Could not load the Database driver!");
			return false;
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Could not initialize the Database!");
			return false;
		}
		
		_persistenceService = new PersistenceService(db);
		return true;
	}
	
	private boolean testDB(PersistenceService ps)
	{
		Challenge c = ChallengeFactory.getIPChallenge();
		Solution s = new Solution("test");
		SolvableChallenge sc = new SolvableChallenge(s, c);
		try
		{
			int cID = ps.getSolvableChallengeMapper().insert(sc);
			ps.getSolvableChallengeMapper().getAll();
			ps.getSolvableChallengeMapper().update(sc, cID);
			ps.getSolvableChallengeMapper().delete(cID);
		} catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Accessing the Database failed!\n" + 
							"Maybe the Database is outdated?");
			return false;
		}
		return true;
	}
	
	private boolean initChallengeRepository(SolvableChallengeMapper mapper)
	{
		try
		{
			_challengeRepository = new ChallengeRepository(mapper);
		} catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error while loading challenges from the Database!");
			return false;
		}
		return true;
	}
	
	private boolean initWordRepository(WordMapper mapper)
	{
		try
		{
			_wordRepository = new WordRepository(mapper);
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error while loading words from the Database!");
			return false;
		}
		return true;
	}
	
	private boolean initChallengeGeneratorService()
	{
		_generator = new ChallengeGeneratorService(_wordRepository);
		return true;
	}
	
	private ServiceContainer createServiceContainer()
	{
		return new ServiceContainer(_settingsService, _persistenceService, _challengeRepository, _wordRepository, _generator);
	}
}
