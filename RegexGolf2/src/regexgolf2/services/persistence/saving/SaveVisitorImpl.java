package regexgolf2.services.persistence.saving;

import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.Word;
import regexgolf2.model.containers.ChallengePool;
import regexgolf2.model.containers.WordPool;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.changetracking.ChangeTrackingService;
import regexgolf2.services.persistence.mappers.Mappers;

public class SaveVisitorImpl implements SaveVisitor
{
	private final ChangeTrackingService _changeTrackingService;
	private final Mappers _mappers;



	public SaveVisitorImpl(ChangeTrackingService changeTrackingService, Mappers mappers)
	{
		_changeTrackingService = changeTrackingService;
		_mappers = mappers;
	}



	@Override
	public void visit(ChallengePool challengePool) throws PersistenceException
	{
		for (SolvableChallenge challenge : challengePool)
			visit(challenge);
	}

	@Override
	public void visit(SolvableChallenge solvableChallenge) throws PersistenceException
	{
		if (!_changeTrackingService.isChanged(solvableChallenge))
			return;

		if (_changeTrackingService.isNew(solvableChallenge))
			_mappers.getSolvableChallengeMapper().insert(solvableChallenge);
		else
			_mappers.getSolvableChallengeMapper().update(solvableChallenge);

		_changeTrackingService.objectWasPersisted(solvableChallenge);
	}

	@Override
	public void visit(WordPool wordPool) throws PersistenceException
	{
		// XXX A large part of the updates that happen here are unnecessary,
		// consider fixing this

		if (_changeTrackingService.isNew(wordPool))
			_mappers.getWordPoolMapper().insert(wordPool);
		else
			_mappers.getWordPoolMapper().update(wordPool);

		for (Word w : wordPool)
		{
			if (!_changeTrackingService.isChanged(w))
				continue;
			
			if (_changeTrackingService.isNew(w))
				_mappers.getWordMapper().insert(w, wordPool.getId());
			else
				_mappers.getWordMapper().update(w, wordPool.getId());
			_changeTrackingService.objectWasPersisted(w);
		}
		
		_changeTrackingService.objectWasPersisted(wordPool);
	}
}
