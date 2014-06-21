package regexgolf2.services.persistence;

import regexgolf2.services.persistence.mappers.ChallengeMapper;
import regexgolf2.services.persistence.mappers.RequirementMapper;
import regexgolf2.services.persistence.mappers.SolutionMapper;
import regexgolf2.services.persistence.mappers.SolvableChallengeMapper;
import regexgolf2.services.persistence.mappers.WordMapper;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class PersistenceService
{
	private final SolutionMapper _solutionMapper;
	private final RequirementMapper _requirementMapper;
	private final ChallengeMapper _challengeMapper;
	private final SolvableChallengeMapper _solvableChallengeMapper;
	private final WordMapper _wordMapper;
	
	
	
	@Requires("db != null")
	public PersistenceService(Database db)
	{
		_solutionMapper = new SolutionMapper(db);
		_requirementMapper = new RequirementMapper(db);
		_challengeMapper = new ChallengeMapper(db, _requirementMapper);
		_solvableChallengeMapper = new SolvableChallengeMapper(_challengeMapper, _solutionMapper);
		_wordMapper = new WordMapper(db);
	}
	
	
	
	@Ensures("result != null")
	public ChallengeMapper getChallengeMapper()
	{
		return _challengeMapper;
	}
	
	@Ensures("result != null")
	public SolvableChallengeMapper getSolvableChallengeMapper()
	{
		return _solvableChallengeMapper;
	}
	
	@Ensures("result != null")
	public WordMapper getWordMapper()
	{
		return _wordMapper;
	}
}
