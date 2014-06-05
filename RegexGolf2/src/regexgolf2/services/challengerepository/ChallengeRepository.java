package regexgolf2.services.challengerepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regexgolf2.model.challenge.Challenge;
import regexgolf2.services.persistence.PersistenceServiceOld;

public class ChallengeRepository
{
	private final PersistenceServiceOld _persistenceService;
	private final Map<Challenge, ChangeTracker> _changeTrackers = new HashMap<>();
	
	public ChallengeRepository(PersistenceServiceOld persistenceService)
	{
		_persistenceService = persistenceService;
		init();
	}
	
	private void init()
	{
		List<Challenge> challenges = _persistenceService.getAll();
		for (Challenge challenge : challenges)
		{
			putChallenge(challenge, false, false);
		}
	}
	
	private void putChallenge(Challenge challenge, boolean isChanged, boolean isNew)
	{
		//FIXME 
		//_changeTrackers.put(challenge, new ChangeTracker(challenge, isChanged, isNew));
	}
	
	public List<Challenge> getAll()
	{
		return new ArrayList<>(_changeTrackers.keySet());
	}
	
	public void save(Challenge challenge)
	{
		boolean successfull = trySave(challenge);
		if (!successfull)
			throw new IllegalArgumentException();
	}

	private boolean trySave(Challenge challenge)
	{
		ChangeTracker ct = _changeTrackers.get(challenge);
		if (ct == null)
			return false;
		
		if (ct.objectIsNew())
		{
			_persistenceService.insert(challenge);
		}
		else
		{
			if (ct.objectIsChanged())
			{
				_persistenceService.update(challenge);
			}
		}	
		ct.changesSaved();
		return true;
	}

	public Challenge addNew()
	{
		//TODO refactor object creation
//		Challenge newChallenge = new Challenge(new Solution(), new Solution());
//		putChallenge(newChallenge, true, true);
//		return newChallenge;
		return null;
	}
	
	public void delete(Challenge challenge)
	{
		if (!_changeTrackers.containsKey(challenge))
			throw new IllegalArgumentException();
		
		//FIXME
		//_persistenceService.delete(challenge.getId());
	}
	
	//can return null!
	public ChangeTracker getChangeTracker(Challenge challenge)
	{
		return _changeTrackers.get(challenge);
	}
}
