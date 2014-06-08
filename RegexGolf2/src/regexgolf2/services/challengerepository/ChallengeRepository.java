package regexgolf2.services.challengerepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ObservableService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper.SolvableChallengeDTO;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * Fires the serviceChangedEvent if a challenge is added or removed.
 */
public class ChallengeRepository extends ObservableService
{
	private final SolvableChallengeMapper _mapper;
	private final Map<SolvableChallenge, Integer> _idMap = new HashMap<>();
	private final Map<SolvableChallenge, PersistenceStateImpl> _persistenceStates = new HashMap<>();
	
	
	
	@Requires("mapper != null")
	public ChallengeRepository(SolvableChallengeMapper mapper) throws SQLException
	{
		_mapper = mapper;
		reloadAll();
	}
	
	
	
	private void reloadAll() throws SQLException
	{
		_idMap.clear();
		_persistenceStates.clear();
		
		List<SolvableChallengeDTO> dtos = new ArrayList<>();
		
		for (SolvableChallengeDTO dto : dtos)
		{
			_idMap.put(dto.challenge, dto.challengeId);
			_persistenceStates.put(dto.challenge, new PersistenceStateImpl(dto.challenge, false));
		}
		fireServiceChangedEvent();
	}
	
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
		_persistenceStates.put(c, new PersistenceStateImpl(c, true));
		fireServiceChangedEvent();
		return c;
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
		_mapper.delete(_idMap.get(c));
		_idMap.remove(c);
		_persistenceStates.remove(c);
		fireServiceChangedEvent();
	}
	
	public boolean contains(SolvableChallenge c)
	{
		return _idMap.keySet().contains(c);
	}
}
