package regexgolf2.services.initializing;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.services.ChangeTrackingService;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.services.settingsservice.SettingsService;

public class ServiceContainer
{
	private final SettingsService _settingsService;
	private final ChangeTrackingService _changeTrackingService;
	private final PersistenceService _persistenceService;
	private final ChallengeRepository _challengeRepository;
	private final WordRepository _wordRepository;
	private final ChallengeGeneratorService _generatorService;
	
	
	
	@Requires({
		"settingsService != null",
		"changeTrackingService != null",
		"persistenceService != null",
		"challengeRepository != null",
		"wordRepository != null",
		"challengeGeneratorService != null"
	})
	public ServiceContainer(
			SettingsService settingsService,
			ChangeTrackingService changeTrackingService,
			PersistenceService persistenceService,
			ChallengeRepository challengeRepository,
			WordRepository wordRepository,
			ChallengeGeneratorService challengeGeneratorService)
	{
		_settingsService = settingsService;
		_changeTrackingService = changeTrackingService;
		_persistenceService = persistenceService;
		_challengeRepository = challengeRepository;
		_wordRepository = wordRepository;
		_generatorService = challengeGeneratorService;
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
		return _generatorService;
	}
}
