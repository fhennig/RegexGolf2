package regexgolf2.services.persistence;

import regexgolf2.services.persistence.mappers.ChallengeMapper;
import regexgolf2.services.persistence.mappers.RequirementMapper;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class PersistenceService
{
	private final RequirementMapper _requirementMapper;
	private final ChallengeMapper _challengeMapper;
	
	
	
	@Requires("db != null")
	public PersistenceService(Database db)
	{
		_requirementMapper = new RequirementMapper(db);
		_challengeMapper = new ChallengeMapper(db, _requirementMapper);
	}
	
	
	
	@Ensures("result != null")
	public ChallengeMapper getChallengeMapper()
	{
		return _challengeMapper;
	}
}
