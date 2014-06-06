package regexgolf2.services.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;

public class ChallengeMapper
{
	private static final int SYSTEM_USER = 0;
	private final Database _db;
	
	
	
	public ChallengeMapper(Database db)
	{
		_db = db;
	}
	
	
	
	/**
	 * Returns a list of all the Challenges stored in the Database.
	 */
	public List<Challenge> getAll()
	{
		List<Challenge> challenges = new ArrayList<>();
		
		String challengeSQL = "SELECT c.id, c.name, s.regex FROM challenges c, solutions s WHERE c.solution = s.id";
		String requirementSQL = "SELECT challenge, expectedMatchResult, word FROM requirements";
		
		try
		{
			PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
			ResultSet challengeRS = challengePS.executeQuery();
			
			while(challengeRS.next())
			{
				Challenge challenge = new Challenge();
				challenge.setId(1);
				challenge.setName(challengeRS.getString(2));
				challenge.getSampleSolution().trySetSolution(challengeRS.getString(3));
				challenges.add(challenge);
			}
			challengePS.close();
			
			PreparedStatement requirementPS = _db.getConnection().prepareStatement(requirementSQL);
			ResultSet requirementRS = requirementPS.executeQuery();
			
			while(requirementRS.next())
			{
				Requirement requirement = new Requirement(
						requirementRS.getBoolean(2), requirementRS.getString(3));
				int challengeId = requirementRS.getInt(1);
				
				for (Challenge c : challenges)
				{
					if (c.getId() == challengeId)
					{
						c.addRequirement(requirement);
					}
				}
			}
			requirementPS.close();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
		return challenges;
	}	
	
	public void insert(Challenge challenge)
	{
		String challengeSQL = "INSERT INTO challenges (id, solution, name) " +
				"VALUES ((SELECT max(id) + 1 FROM challenges), (SELECT max(id) + 1 FROM solutions), ?);";
		String solutionSQL = "INSERT INTO solutions (id, challenge, user, regex) " +
				"VALUES ((SELECT max(id) + 1 FROM solutions), (SELECT max(id) FROM challenges), ?, ?);";
		String requirementSQL = "INSERT INTO requirements (challenge, expectedMatchResult, word) " +
				"VALUES ((SELECT max(id) FROM challenges), ?, ?);";
		
		try
		{
			_db.getConnection().setAutoCommit(false);
			
			PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
			challengePS.setString(1, challenge.getName());
			challengePS.execute();
			
			PreparedStatement solutionPS = _db.getConnection().prepareStatement(solutionSQL);
			solutionPS.setInt(1, SYSTEM_USER);
			solutionPS.setString(2, challenge.getSampleSolution().getSolution());
			solutionPS.execute();
			
			_db.getConnection().commit();
			_db.getConnection().setAutoCommit(true);
			
			PreparedStatement requirementPS = _db.getConnection().prepareStatement(requirementSQL);
			for (Requirement requirement : challenge.getRequirements())
			{
				requirementPS.setBoolean(1, requirement.getExpectedMatchResult());
				requirementPS.setString(2, requirement.getWord());
				requirementPS.execute();
			}
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
		finally
		{
			try
			{
				_db.getConnection().setAutoCommit(true);
			}
			catch (SQLException e)
			{
				throw new DatabaseException(e);
			}
		}
	}
}
