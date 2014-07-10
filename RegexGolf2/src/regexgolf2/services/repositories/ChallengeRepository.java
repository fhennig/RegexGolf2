package regexgolf2.services.repositories;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import regexgolf2.model.Container;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ChangeTrackingService;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * Fires the serviceChangedEvent if a challenge is added or removed, and if the
 * persistence state of a contained challenge changes.
 */
public class ChallengeRepository extends Container<SolvableChallenge>
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
		_dbMapper.getAll().forEach(dto -> insert(dto.challenge, dto.challengeId, false));
	}

	@Requires(
	{ "c != null", "contains(c)" })
	@Ensures("result != null")
	private PersistenceState getPersistenceState(SolvableChallenge c)
	{
		return _changeTrackingService.getPersistenceState(c);
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

	@Requires(
	{ "challenge != null", "!contains(challenge)" })
	@Ensures("contains(challenge)")
	private void insert(SolvableChallenge challenge, int id, boolean isNew)
	{
		_idMap.put(challenge, id);
		_changeTrackingService.track(challenge, isNew);		
		super.add(challenge);
	}

	/**
	 * Saves all changed Challenges
	 * 
	 * @throws SQLException
	 */
	public void saveAll() throws SQLException
	{
		for (SolvableChallenge challenge : _idMap.keySet())
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
		super.remove(c);
	}
}
