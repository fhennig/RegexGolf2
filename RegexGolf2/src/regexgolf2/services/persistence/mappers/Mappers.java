package regexgolf2.services.persistence.mappers;

import regexgolf2.services.persistence.database.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * Wrapper class for all the Mapper objects needed.
 * This class initializes the Mappers and provides access to high-level mappers.
 */
public class Mappers
{
	private final SolutionMapper _solutionMapper;
	private final RequirementMapper _requirementMapper;
	private final ChallengeMapper _challengeMapper;
	private final SolvableChallengeMapper _solvableChallengeMapper;
	private final WordMapper _wordMapper;
	private final WordPoolMapper _wordPoolMapper;
	
	
	
	@Requires("db != null")
	public Mappers(Database db)
	{
		_solutionMapper = new SolutionMapper(db);
		_requirementMapper = new RequirementMapper(db);
		_challengeMapper = new ChallengeMapper(db, _requirementMapper);
		_solvableChallengeMapper = new SolvableChallengeMapper(_challengeMapper, _solutionMapper);
		_wordMapper = new WordMapper(db);
		_wordPoolMapper = new WordPoolMapper(db, _wordMapper);
	}
	
	
	
	@Ensures("result != null")
	public SolvableChallengeMapper getSolvableChallengeMapper()
	{
		return _solvableChallengeMapper;
	}

	@Ensures("result != null")
	public WordPoolMapper getWordPoolMapper()
	{
		return _wordPoolMapper;
	}

	@Ensures("result != null")
	public WordMapper getWordMapper()
	{
		return _wordMapper;
	}
}
