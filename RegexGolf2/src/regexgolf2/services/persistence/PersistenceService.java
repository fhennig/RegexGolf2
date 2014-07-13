package regexgolf2.services.persistence;

import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.Word;
import regexgolf2.model.containers.ChallengePool;
import regexgolf2.model.containers.ContainerChangedListener;
import regexgolf2.model.containers.WordPool;
import regexgolf2.model.containers.WordPoolContainer;
import regexgolf2.services.persistence.changetracking.ChangeTrackingService;
import regexgolf2.services.persistence.changetracking.PersistenceInformation;
import regexgolf2.services.persistence.mappers.Mappers;
import regexgolf2.services.persistence.saving.Savable;
import regexgolf2.services.persistence.saving.SaveVisitorImpl;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class PersistenceService
{
	private final Mappers _mappers;

	private final ChangeTrackingService _changeTrackingService;
	private final TrackHandler _trackHandler;

	private final SaveVisitorImpl _saveVisitor;

	private final ChallengePool _challengePool;
	private final WordPoolContainer _wordPoolContainer;



	@Requires("mappers != null")
	public PersistenceService(Mappers mappers) throws PersistenceException
	{
		_mappers = mappers;

		_changeTrackingService = new ChangeTrackingService();
		_trackHandler = new TrackHandler(_changeTrackingService);

		_saveVisitor = new SaveVisitorImpl(_changeTrackingService, _mappers);

		_challengePool = createChallengePool();
		_wordPoolContainer = createWordPoolContainer();
	}



	private WordPoolContainer createWordPoolContainer() throws PersistenceException
	{
		WordPoolContainer wpc = new WordPoolContainer();

		for (WordPool wp : _mappers.getWordPoolMapper().getAll())
		{
			wpc.add(wp);
			_changeTrackingService.track(wp, false);
			wp.forEach(word -> _changeTrackingService.track(word, false));
			addWordPoolListeners(wp);
		}

		DeleteHandler<WordPool> deleteHandler = new DeleteHandler<WordPool>(
				getPersistenceInformation(), wp -> _mappers.getWordPoolMapper().delete(wp));
		
		wpc.addListener(event -> {
			deleteHandler.containerChanged(event);
			_trackHandler.containerChanged(event);
		});

		return wpc;
	}

	private void addWordPoolListeners(final WordPool wp)
	{
		DeleteHandler<Word> deleteHandler = new DeleteHandler<>(getPersistenceInformation(),
				word -> _mappers.getWordMapper().delete(word, wp.getId()));

		wp.addListener(event ->
		{
			deleteHandler.containerChanged(event);
			_trackHandler.containerChanged(event);
		});
	}

	private ChallengePool createChallengePool() throws PersistenceException
	{
		ChallengePool pool = new ChallengePool();
		for (SolvableChallenge challenge : _mappers.getSolvableChallengeMapper().getAll())
		{
			pool.add(challenge);
			_changeTrackingService.track(challenge, false);
		}
		ContainerChangedListener<SolvableChallenge> deleteHandler = new DeleteHandler<>(
				getPersistenceInformation(),
				challenge -> _mappers.getSolvableChallengeMapper().delete(challenge));
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
		return _challengePool;
	}

	@Ensures("result != null")
	public WordPoolContainer getWordPoolContainer()
	{
		return _wordPoolContainer;
	}

	@Ensures("result != null")
	public PersistenceInformation getPersistenceInformation()
	{
		return _changeTrackingService;
	}
}
