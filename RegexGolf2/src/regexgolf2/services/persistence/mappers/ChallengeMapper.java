package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;
import regexgolf2.services.persistence.Database;
import regexgolf2.services.persistence.DatabaseException;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeMapper
{
	private final Database _db;
	
	
	
	public ChallengeMapper(Database db)
	{
		_db = db;
	}
	
	
	
	/**
	 * Returns a list of all the Challenges stored in the Database.
	 */
	@Ensures("result != null")
	public List<Challenge> getAll()
	{
		List<Challenge> challenges = new ArrayList<>();
		
		String challengeSQL = "SELECT id, regex, name FROM challenges";
		String requirementSQL = "SELECT challenge, expectedMatchResult, word FROM requirements";
		
		try
		{
			PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
			ResultSet challengeRS = challengePS.executeQuery();
			
			while(challengeRS.next())
			{
				Challenge challenge = new Challenge();
				challenge.setId(challengeRS.getInt(1));
				challenge.setName(challengeRS.getString(3));
				challenge.getSampleSolution().trySetSolution(challengeRS.getString(2));
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
	
	/**
	 * Inserts a Challenge into the database.
	 * The Challenge gets an ID that is written into the given object.
	 */
	@Requires("challenge != null")
	public void insert(Challenge challenge)
	{
		challenge.setId(getNextChallengeId());
		
		String challengeSQL = "INSERT INTO challenges (id, regex, name) VALUES (?, ?, ?);";
		
		try
		{
			
			PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
			challengePS.setInt(1, challenge.getId());
			challengePS.setString(2, challenge.getSampleSolution().getSolution());
			challengePS.setString(3, challenge.getName());
			challengePS.execute();
			challengePS.close();
			
			insertRequirements(challenge.getRequirements(), challenge.getId());
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}
	
	private int getNextChallengeId()
	{
		String sql = "SELECT CASE WHEN count(*) = 0 THEN 1 ELSE max(id) + 1 END FROM challenges";
		try
		{
			PreparedStatement nextIdPS = _db.getConnection().prepareStatement(sql);
			ResultSet rs = nextIdPS.executeQuery();
			int nextId = rs.getInt(1);
			rs.close();
			return nextId;
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}
	
	@Requires("challenge != null")
	public void update(Challenge challenge)
	{
		String challengeSQL = "UPDATE challenges SET regex=?, name=? WHERE id=?";
		
		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
			ps.setString(1, challenge.getSampleSolution().getSolution());
			ps.setString(2, challenge.getName());
			ps.setInt(3, challenge.getId());
			ps.execute();
			ps.close();
			removeRequirements(challenge.getId());
			insertRequirements(challenge.getRequirements(), challenge.getId());
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}
	
	public void delete(int challengeId)
	{
		String challengeSQL = "DELETE FROM challenges WHERE id = ?";
		
		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
			ps.setInt(1, challengeId);
			ps.execute();
			
			//requirements are deleted via cascade by the database itself
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}

	@Requires("requirements != null")
	private void insertRequirements(Set<Requirement> requirements, int challengeId)
	{
		String requirementSQL = "INSERT INTO requirements (challenge, expectedMatchResult, word) VALUES (?, ?, ?);";
		
		try
		{
			PreparedStatement requirementPS = _db.getConnection().prepareStatement(requirementSQL);
			for (Requirement requirement : requirements)
			{
				requirementPS.setInt(1, challengeId);
				requirementPS.setBoolean(2, requirement.getExpectedMatchResult());
					requirementPS.setString(3, requirement.getWord());
				requirementPS.execute();
			}
			requirementPS.close();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
	}

	private void removeRequirements(int challengeId)
	{
		String sql = "DELETE FROM requirements WHERE challenge = ?";
		
		try
		{
			PreparedStatement ps = _db.getConnection().prepareStatement(sql);
			ps.setInt(1, challengeId);
			ps.execute();
			ps.close();
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
		
	}
}
