package regexgolf2.services.persistence;

import java.sql.SQLException;

import regexgolf2.model.ChallengePool;
import regexgolf2.model.ContainerChangedListener;
import regexgolf2.model.ObservableObject;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.changetracking.ChangeTrackingService;
import regexgolf2.services.persistence.changetracking.PersistenceState;
import regexgolf2.services.persistence.changetracking.PersistenceStateSupplier;
import regexgolf2.services.persistence.mappers.Mappers;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.WordMapper;
import regexgolf2.services.persistence.saving.Savable;
import regexgolf2.services.persistence.saving.SaveVisitorImpl;
import regexgolf2.services.repositories.WordRepository;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class PersistenceService implements PersistenceStateSupplier
{
	private final Mappers _mappers;

	private final ChangeTrackingService _changeTrackingService;
	private final TrackHandler _trackHandler;

	private final SaveVisitorImpl _saveVisitor;

	private final ChallengePool _challengeRepository;
	private final WordRepository _wordRepository;



	@Requires("mappers != null")
	public PersistenceService(Mappers mappers) throws PersistenceException
	{
		_mappers = mappers;

		_changeTrackingService = new ChangeTrackingService();
		_trackHandler = new TrackHandler(_changeTrackingService);

		// TODO dont use this reference, let cts implement the required
		// interface and pass it in
		_saveVisitor = new SaveVisitorImpl(_changeTrackingService, _mappers);

		_challengeRepository = createChallengePool();
		_wordRepository = new WordRepository(_mappers.getWordMapper());
	}



	private ChallengePool createChallengePool() throws PersistenceException
	{
		ChallengePool pool = new ChallengePool();
		try
		{
			for (SolvableChallenge challenge : _mappers.getSolvableChallengeMapper().getAll())
			{
				pool.add(challenge);
				_changeTrackingService.track(challenge, false);
			}
		} catch (SQLException e)
		{
			throw new PersistenceException();
		}
		ContainerChangedListener<SolvableChallenge> deleteHandler = new DeleteHandler<>(
				_changeTrackingService, _mappers.getSolvableChallengeMapper());
		// Because the deleteHandler needs to access PersistenceStates for
		// removed items,
		// the trackHandler needs to untrack the item after the
		// deleteHandler was called.
		pool.addListener(event ->
		{
			deleteHandler.containerChanged(event);
			_trackHandler.containerChanged(event);
		});
		return pool;
	}


	@Requires("savable != null")
	public void save(Savable savable) throws PersistenceException
	{
		savable.accept(_saveVisitor);
	}

	@Ensures("result != null")
	public ChallengePool getChallengePool()
	{
		return _challengeRepository;
	}

	@Ensures("result != null")
	public WordRepository getWordRepository()
	{
		return _wordRepository;
	}

	@Requires(
	{ "isTracked(object)", "object != null" })
	@Ensures("result != null")
	public PersistenceState getPersistenceState(ObservableObject object)
	{
		return _changeTrackingService.getPersistenceState(object);
	}

	@Override
	@Requires("object != null")
	public boolean isTracked(ObservableObject object)
	{
		return _changeTrackingService.isTracked(object);
	}

	@Ensures("result != null")
	public SolvableChallengeMapper getSolvableChallengeMapper()
	{
		return _mappers.getSolvableChallengeMapper();
	}

	@Ensures("result != null")
	public WordMapper getWordMapper()
	{
		return _mappers.getWordMapper();
	}



	// TODO this is not a good name. this is also not a good place for this
	// method
	@Override
	public PersistenceState getFor(ObservableObject object)
	{
		return getPersistenceState(object);
	}
}
