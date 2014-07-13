package regexgolf2.services.persistence.saving;

import regexgolf2.model.SolvableChallenge;
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
		if (!_changeTrackingService.isChanged(wordPool))
			return;
		
		if (_changeTrackingService.isNew(wordPool))
			_mappers.getWordPoolMapper().insert(wordPool);
		else
			_mappers.getWordPoolMapper().update(wordPool);
			
		_changeTrackingService.objectWasPersisted(wordPool);
		wordPool.forEach(word -> _changeTrackingService.objectWasPersisted(word));
	}
}
