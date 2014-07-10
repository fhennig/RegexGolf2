package regexgolf2.services.repositories;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ChangeTrackingService;
import regexgolf2.services.ObservableService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper.SolvableChallengeDTO;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * Fires the serviceChangedEvent if a challenge is added or removed, and if the
 * persistence state of a contained challenge changes.
 */
public class ChallengeRepository extends ObservableService
{
	private final SolvableChallengeMapper _dbMapper;
	private final Map<SolvableChallenge, Integer> _idMap = new HashMap<>();
	private final ChangeTrackingService _changeTrackingService;



	@Requires({
		"mapper != null",
		"cts != null"
	})
	public ChallengeRepository(SolvableChallengeMapper mapper, ChangeTrackingService cts) throws SQLException
	{
		_changeTrackingService = cts;
		_dbMapper = mapper;
		reloadAll();
	}



	private void reloadAll() throws SQLException
	{
		// _idMap.clear();
		// _persistenceStates.clear();
		clearAll();


		List<SolvableChallengeDTO> dtos = _dbMapper.getAll();

		for (SolvableChallengeDTO dto : dtos)
		{
			// XXX because the dto is just public fields, we can not be sure
			// that the values are != null;
			// / contract model ist harder to apply
			// _idMap.put(dto.challenge, dto.challengeId);
			insert(dto.challenge, dto.challengeId, false);
		}
		// event is fired in insert
		// fireServiceChangedEvent();
	}

	/**
	 * Returns an <b>unmodifiable</b> Set.
	 */
	@Ensures("result != null")
	public Set<SolvableChallenge> getAll()
	{
		return Collections.unmodifiableSet(_idMap.keySet());
	}

	private void clearAll()
	{
		for (SolvableChallenge challenge : _idMap.keySet())
			_changeTrackingService.untrack(challenge);
		_idMap.clear();
	}

	@Requires(
	{ "c != null", "contains(c)" })
	@Ensures("result != null")
	private PersistenceState getPersistenceState(SolvableChallenge c)
	{
		return _changeTrackingService.getPersistenceState(c);
		// return _persistenceStates.get(c);
	}

	@Ensures("result != null")
	public SolvableChallenge createNew()
	{
		SolvableChallenge c = new SolvableChallenge();
		insert(c);
		return c;
	}

	@Requires(
	{ "challenge != null", "!contains(challenge)" })
	@Ensures("contains(challenge)")
	private void insert(SolvableChallenge challenge)
	{
		insert(challenge, 0, true);
	}

	private void insert(SolvableChallenge challenge, int id, boolean isNew)
	{
		_idMap.put(challenge, id);
		_changeTrackingService.track(challenge, isNew);
		fireServiceChangedEvent();
	}

	/**
	 * Saves all changed Challenges
	 * 
	 * @throws SQLException
	 */
	public void saveAll() throws SQLException
	{
		for (SolvableChallenge challenge : getAll())
		{
			if (getPersistenceState(challenge).isChanged())
			{
				save(challenge);
			}
		}
	}

	@Requires(
	{ "c != null" })
	@Ensures(
	{ "getPersistenceState(c).isNew() == false", "contains(c)" })
	public void save(SolvableChallenge c) throws SQLException
	{
		if (!contains(c))
			insert(c);

		boolean isNew = getPersistenceState(c).isNew();

		if (isNew)
		{
			int id = _dbMapper.insert(c);
			_idMap.put(c, id);
		} else
		{
			_dbMapper.update(c, _idMap.get(c));
		}
		// TODO remove cast
		((PersistenceStateImpl) getPersistenceState(c)).objectWasPersisted();
	}

	@Requires(
	{ "c != null", "contains(c)" })
	@Ensures("!contains(c)")
	public void delete(SolvableChallenge c) throws SQLException
	{
		if (!getPersistenceState(c).isNew())
			_dbMapper.delete(_idMap.get(c));
		_idMap.remove(c);
		_changeTrackingService.untrack(c);
		fireServiceChangedEvent();
	}

	/**
	 * Returns true if the given Challenge is managed by this
	 * ChallengeRepository, false otherwise.
	 */
	public boolean contains(SolvableChallenge c)
	{
		if (c == null)
			return false;
		return _idMap.keySet().contains(c);
	}
}
