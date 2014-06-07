package regexgolf2.services.persistence.mappers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.mappers.ChallengeMapper.ChallengeDTO;
import regexgolf2.services.persistence.mappers.SolutionMapper.SolutionDTO;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class SolvableChallengeMapper
{
	private final ChallengeMapper _challenges;
	private final SolutionMapper _solutions;
	
	
	
	public SolvableChallengeMapper(ChallengeMapper cMapper, SolutionMapper sMapper)
	{
		_challenges = cMapper;
		_solutions = sMapper;
	}
	
	
	
	@Ensures("result != null")
	public List<SolvableChallengeDTO> getAll() throws SQLException
	{
		List<SolvableChallengeDTO> dtos = new ArrayList<>();
		
		List<ChallengeDTO> challenges = _challenges.getAll();
		List<SolutionDTO> solutions = _solutions.getAll();
		
		for (ChallengeDTO c : challenges)
		{
			SolvableChallengeDTO dto = new SolvableChallengeDTO();
			dto.challengeId = c.id;
			for (SolutionDTO s : solutions)
			{
				if (s.challengeId == dto.challengeId)
				{
					dto.challenge = new SolvableChallenge(s.solution, c.challenge);
				}
			}
			if (dto.challenge == null)
				throw new IllegalStateException("It is missing a Solution for the challengeId=" + dto.challengeId);
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	/**
	 * Inserts the containing Solution and Challenge into the database.
	 * @return the id that was assigned to the challenge on inserting it
	 * @throws SQLException 
	 */
	@Requires("challenge != null")
	public int insert(SolvableChallenge challenge) throws SQLException
	{
		int id = _challenges.insert(challenge.getChallenge());
		_solutions.insert(challenge.getSolution(), id);
		return id;
	}
	
	public void update(SolvableChallenge challenge, int challengeId) throws SQLException
	{
		_challenges.update(challenge.getChallenge(), challengeId);
		_solutions.update(challenge.getSolution(), challengeId);
	}
	
	public void delete(int challengeId) throws SQLException
	{
		_challenges.delete(challengeId);
		//deleting the solution is done via cascade by the database itself
	}
	
	
	
	public class SolvableChallengeDTO
	{
		public int challengeId;
		public SolvableChallenge challenge;
	}
}
