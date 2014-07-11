package regexgolf2.services.persistence.mappers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.PersistenceException;
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
	public List<SolvableChallenge> getAll() throws SQLException
	{
		List<SolvableChallenge> solvChallenges = new ArrayList<>();

		List<Challenge> challenges = _challenges.getAll();
		List<SolutionDTO> solutions = _solutions.getAll();

		for (Challenge c : challenges)
		{
			SolvableChallenge sc = null;
			for (SolutionDTO s : solutions)
			{
				if (s.challengeId == c.getId())
				{
					sc = new SolvableChallenge(s.solution, c);
				}
			}
			if (sc == null)
				throw new IllegalStateException("It is missing a Solution for the challengeId="
						+ c.getId());
			solvChallenges.add(sc);
		}

		return solvChallenges;
	}

	/**
	 * Inserts the containing Solution and Challenge into the database.
	 * 
	 * @return the id that was assigned to the challenge on inserting it
	 * @throws SQLException
	 */
	@Requires("challenge != null")
	public void insert(SolvableChallenge challenge) throws PersistenceException
	{
		try
		{
			_challenges.insert(challenge.getChallenge());
			_solutions.insert(challenge.getSolution(), challenge.getChallenge().getId());
		} catch (SQLException e)
		{
			throw new PersistenceException(e);
		}
	}

	public void update(SolvableChallenge challenge) throws PersistenceException
	{
		try
		{
			_challenges.update(challenge.getChallenge());
			_solutions.update(challenge.getSolution(), challenge.getChallenge().getId());
		} catch (SQLException e)
		{
			throw new PersistenceException(e);
		}
	}

	public void delete(SolvableChallenge challenge) throws PersistenceException
	{
		try
		{
			_challenges.delete(challenge.getChallenge());
			// deleting the solution is done via cascade by the database itself
		} catch (SQLException e)
		{
			throw new PersistenceException(e);
		}
	}
}
