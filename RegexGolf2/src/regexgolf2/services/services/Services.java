package regexgolf2.services.services;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import regexgolf2.model.Challenge;
import regexgolf2.model.ContainerChangedListener;
import regexgolf2.model.ObservableObject;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ChangeTrackingService;
import regexgolf2.services.DeleteHandler;
import regexgolf2.services.PersistenceException;
import regexgolf2.services.TrackHandler;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.persistence.Database;
import regexgolf2.services.persistence.DatabaseInitializer;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.WordMapper;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.services.settingsservice.SettingsService;
import regexgolf2.startup.ChallengeFactory;

import com.google.java.contract.Ensures;

public class Services
{
	private static final Logger _LOG = Logger.getLogger(Services.class.getName());

	private SettingsService _settingsService;
	private ChangeTrackingService _changeTrackingService;
	private PersistenceService _persistenceService;
	private ChallengeRepository _challengeRepository;
	private WordRepository _wordRepository;
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

			if (!initChangeTrackingService())
				throw new InitializingException();
			_LOG.info("ChangeTrackingService initialized");

			if (!initPersistenceService(_settingsService.getSettings().getSQLiteDBPath()))
				throw new InitializingException();
			_LOG.info("PersistenceService initialized");

			if (!testDB(_persistenceService))
				throw new InitializingException();
			_LOG.info("Database Test successful");

			if (!initChallengeRepository(_persistenceService.getSolvableChallengeMapper()))
				throw new InitializingException();
			_LOG.info("ChallengeRepository initialized");

			if (!initWordRepository(_persistenceService.getWordMapper()))
				throw new InitializingException();
			_LOG.info("WordRepository initialized");

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

	private boolean initChangeTrackingService()
	{
		_changeTrackingService = new ChangeTrackingService();
		return true;
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
			ps.getSolvableChallengeMapper().insert(sc);
			ps.getSolvableChallengeMapper().getAll();
			ps.getSolvableChallengeMapper().update(sc);
			ps.getSolvableChallengeMapper().delete(sc);
		} catch (SQLException | PersistenceException e)
		{
			JOptionPane.showMessageDialog(null, "Accessing the Database failed!\n"
					+ "Maybe the Database is outdated?");
			return false;
		}
		return true;
	}

	private boolean initChallengeRepository(SolvableChallengeMapper mapper)
	{
		try
		{
			_challengeRepository = new ChallengeRepository(mapper, _changeTrackingService);
			_challengeRepository.forEach(challenge -> _changeTrackingService
					.track(challenge, false));
			
			
			ContainerChangedListener<ObservableObject> trackHandler = new TrackHandler(_changeTrackingService);
			ContainerChangedListener<SolvableChallenge> deleteHandler = new DeleteHandler<>(_changeTrackingService, mapper);
			//Because the deleteHandler needs to access PersistenceStates for removed items,
			//the trackHandler needs to untrack the item after the deleteHandler was called.
			_challengeRepository.addListener(event -> {
				deleteHandler.containerChanged(event);
				trackHandler.containerChanged(event);
			});
			} catch (SQLException e)
		{
			JOptionPane
					.showMessageDialog(null, "Error while loading challenges from the Database!");
			return false;
		}
		return true;
	}

	private boolean initWordRepository(WordMapper mapper)
	{
		try
		{
			_wordRepository = new WordRepository(mapper);
		} catch (SQLException e)
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

	@Ensures("result != null")
	public SettingsService getSettingsService()
	{
		return _settingsService;
	}

	@Ensures("result != null")
	public ChangeTrackingService getChangeTrackingService()
	{
		return _changeTrackingService;
	}

	@Ensures("result != null")
	public PersistenceService getPersistenceService()
	{
		return _persistenceService;
	}

	@Ensures("result != null")
	public ChallengeRepository getChallengeRepository()
	{
		return _challengeRepository;
	}

	@Ensures("result != null")
	public WordRepository getWordRepository()
	{
		return _wordRepository;
	}

	@Ensures("result != null")
	public ChallengeGeneratorService getGeneratorService()
	{
		return _generator;
	}
}
