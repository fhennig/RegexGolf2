package regexgolf2.services.initializing;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.services.challengerepository.ChallengeRepository;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.settingsservice.SettingsService;

public class ServiceContainer
{
	private final SettingsService _settingsService;
	private final PersistenceService _persistenceService;
	private final ChallengeRepository _challengeRepository;
	
	
	
	@Requires({
		"settingsService != null",
		"persistenceService != null",
		"challengeRepository != null"
	})
	public ServiceContainer(
			SettingsService settingsService,
			PersistenceService persistenceService,
			ChallengeRepository challengeRepository)
	{
		_settingsService = settingsService;
		_persistenceService = persistenceService;
		_challengeRepository = challengeRepository;
	}
	
	
	
	@Ensures("result != null")
	public SettingsService getSettingsService()
	{
		return _settingsService;
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
}
