package regexgolf2.services.challengerepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regexgolf2.model.Challenge;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.persistence.mappers.ChallengeMapper;

import com.google.java.contract.Requires;

public class ChallengeRepository
{
	private final PersistenceService _persistence;
	private final List<SolvableChallenge> _solvableChallenges = new ArrayList<>();
	private final Map<SolvableChallenge, PersistenceState> _persistenceStates = new HashMap<>();
	
	
	
	@Requires("persistenceService != null")
	public ChallengeRepository(PersistenceService persistenceService)
	{
		_persistence = persistenceService;
	}
	
	
	
	private void reloadAll() throws SQLException
	{
		_solvableChallenges.clear();

		
	}
}
