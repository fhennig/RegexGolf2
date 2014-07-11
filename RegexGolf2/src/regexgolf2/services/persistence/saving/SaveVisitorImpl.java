package regexgolf2.services.persistence.saving;

import java.sql.SQLException;

import regexgolf2.model.ChallengePool;
import regexgolf2.model.SolvableChallenge;
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
		
		try
		{
			if (_changeTrackingService.isNew(solvableChallenge))
				_mappers.getSolvableChallengeMapper().insert(solvableChallenge);
			else
				_mappers.getSolvableChallengeMapper().update(solvableChallenge);
			
			_changeTrackingService.objectWasPersisted(solvableChallenge);
		}
		catch (SQLException ex)
		{
			throw new PersistenceException(ex);
		}
	}
}
