package regexgolf2.services.repositories;

import java.sql.SQLException;

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
	private final ChangeTrackingService _changeTrackingService;



	@Requires(
	{ "mapper != null", "cts != null" })
	public ChallengeRepository(SolvableChallengeMapper mapper, ChangeTrackingService cts) throws SQLException
	{
		_changeTrackingService = cts;
		_dbMapper = mapper;
		_dbMapper.getAll().forEach(sc -> add(sc));
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
		add(c);
		return c;
	}

	/**
	 * Saves all changed Challenges
	 * 
	 * @throws SQLException
	 */
	public void saveAll() throws SQLException
	{
		for (SolvableChallenge challenge : this)
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
			add(c);

		boolean isNew = getPersistenceState(c).isNew();

		if (isNew)
		{
			_dbMapper.insert(c);
		} else
		{
			_dbMapper.update(c);
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
			_dbMapper.delete(c);
		super.remove(c);
	}
}
