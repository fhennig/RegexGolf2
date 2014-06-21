package regexgolf2.services.repositories;

import java.sql.SQLException;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexgolf2.model.Challenge;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ObservableService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper.SolvableChallengeDTO;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * Fires the serviceChangedEvent if a challenge is added or removed,
 * and if the persistence state of a contained challenge changes.
 */
public class ChallengeRepository extends ObservableService
{
	private final SolvableChallengeMapper _mapper;
	private final Map<SolvableChallenge, Integer> _idMap = new HashMap<>();
	private final Map<SolvableChallenge, PersistenceStateImpl> _persistenceStates = new HashMap<>();
	private ObjectChangedListener _persistenceStateListener;
	
	
	
	@Requires("mapper != null")
	public ChallengeRepository(SolvableChallengeMapper mapper) throws SQLException
	{
		_mapper = mapper;
		initPersistenceStateListener();
		reloadAll();
	}
	
	
	
	private void initPersistenceStateListener()
	{
		_persistenceStateListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				fireServiceChangedEvent();
			}
		};
	}
	
	private void reloadAll() throws SQLException
	{
		_idMap.clear();
		_persistenceStates.clear();
		
		List<SolvableChallengeDTO> dtos = _mapper.getAll();
		
		for (SolvableChallengeDTO dto : dtos)
		{
			_idMap.put(dto.challenge, dto.challengeId);
			addPersistenceState(dto.challenge, false);
		}
		fireServiceChangedEvent();
	}
	
	/**
	 * Returns an <b>unmodifiable</b> Set.
	 */
	@Ensures("result != null")
	public Set<SolvableChallenge> getAll()
	{
		return Collections.unmodifiableSet(_idMap.keySet());
	}

	@Requires({
		"c != null",
		"contains(c)"
	})
	public PersistenceState getPersistenceState(SolvableChallenge c)
	{
		return _persistenceStates.get(c);
	}
	
	@Ensures("result != null")
	public SolvableChallenge createNew()
	{
		SolvableChallenge c = new SolvableChallenge(new Solution(), new Challenge());
		_idMap.put(c, 0);
		addPersistenceState(c, true);
		fireServiceChangedEvent();
		return c;
	}

	/**
	 * Saves all changed Challenges
	 * @throws SQLException 
	 */
	public void saveAll() throws SQLException
	{
		for (SolvableChallenge challenge : getAll())
		{
			if (_persistenceStates.get(challenge).isChanged())
			{
				save(challenge);
			}
		}
	}
	
	@Requires({
		"c != null",
		"contains(c)"
	})
	public void save(SolvableChallenge c) throws SQLException
	{
		boolean isNew = _persistenceStates.get(c).isNew();
		
		if (isNew)
		{
			int id = _mapper.insert(c);
			_idMap.put(c, id);
		}
		else
		{
			_mapper.update(c, _idMap.get(c));
		}
		_persistenceStates.get(c).objectWasPersisted();
	}
	
	@Requires({
		"c != null",
		"contains(c)"
	})
	public void delete(SolvableChallenge c) throws SQLException
	{
		if (!getPersistenceState(c).isNew())
			_mapper.delete(_idMap.get(c));
		_idMap.remove(c);
		removePersistenceState(c);
		fireServiceChangedEvent();
	}
	
	/**
	 * Returns true if the given Challenge is managed by this ChallengeRepository,
	 * false otherwise.
	 */
	public boolean contains(SolvableChallenge c)
	{
		return _idMap.keySet().contains(c);
	}
	
	private void addPersistenceState(SolvableChallenge c, boolean isNew)
	{
		PersistenceStateImpl pState = new PersistenceStateImpl(c, isNew);
		pState.addObjectChangedListener(_persistenceStateListener);
		_persistenceStates.put(c, pState);
	}
	
	private void removePersistenceState(SolvableChallenge c)
	{
		_persistenceStates.get(c).removeObjectChangedListener(_persistenceStateListener);
		_persistenceStates.remove(c);
	}
}
