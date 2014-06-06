package regexgolf2.services.persistence;

import regexgolf2.services.persistence.mappers.ChallengeMapper;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class PersistenceService
{
	private final ChallengeMapper _challengeMapper;
	
	
	
	@Requires("db != null")
	public PersistenceService(Database db)
	{
		_challengeMapper = new ChallengeMapper(db);
	}
	
	
	
	@Ensures("result != null")
	public ChallengeMapper getChallengeMapper()
	{
		return _challengeMapper;
	}
}
