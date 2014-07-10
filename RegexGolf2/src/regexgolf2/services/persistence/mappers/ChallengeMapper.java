package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;
import regexgolf2.services.persistence.database.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeMapper
{
	private final Database _db;
	private final RequirementMapper _requirements;
	
	
	
	public ChallengeMapper(Database db, RequirementMapper requirements)
	{
		_db = db;
		_requirements = requirements;
	}
	
	
	
	/**
	 * Returns a list of all the Challenges stored in the Database.
	 * @throws SQLException If initializing failed
	 */
	@Ensures("result != null")
	public List<Challenge> getAll() throws SQLException
	{
		List<Challenge> challenges = new ArrayList<>();
		
		String challengeSQL = "SELECT id, regex, name FROM challenges;";
		
		PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
		ResultSet challengeRS = challengePS.executeQuery();
		
		while(challengeRS.next())
		{
			Challenge challenge = new Challenge();
			challenge.setId(challengeRS.getInt(1));
			challenge.getSampleSolution().trySetSolution(challengeRS.getString(2));
			challenge.setName(challengeRS.getString(3));
			challenges.add(challenge);
		}
		challengePS.close();
		
		for (Challenge challenge : challenges)
		{
			//XXX Database requests inside a loop ... is viable with SQLite?
			List<Requirement> cRequirements = _requirements.getAllFor(challenge.getId());
			for (Requirement r : cRequirements)
				challenge.addRequirement(r);
		}
		return challenges;
	}	
	
	/**
	 * Inserts a Challenge into the database.
	 * The Challenge gets an ID that is written into the given object.
	 * @throws SQLException If initializing failed
	 */
	@Requires("challenge != null")
	public void insert(Challenge challenge) throws SQLException
	{
		challenge.setId(getNextChallengeId());
		
		String challengeSQL = "INSERT INTO challenges (id, regex, name) VALUES (?, ?, ?);";
		
		PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
		challengePS.setInt(1, challenge.getId());
		challengePS.setString(2, challenge.getSampleSolution().getSolution());
		challengePS.setString(3, challenge.getName());
		challengePS.execute();
		challengePS.close();

		_requirements.insert(challenge.getRequirements(), challenge.getId());
	}
	
	/**
	 * Simply selects the max id and adds 1.
	 * This is not safe for concurrency!
	 * Currently, this Application is designed without support for concurrent database access.
	 */
	private int getNextChallengeId() throws SQLException
	{
		String sql = "SELECT CASE WHEN count(*) = 0 THEN 1 ELSE max(id) + 1 END FROM challenges";

		PreparedStatement nextIdPS = _db.getConnection().prepareStatement(sql);
		ResultSet rs = nextIdPS.executeQuery();
		int nextId = rs.getInt(1);
		rs.close();
		return nextId;
	}
	
	@Requires("challenge != null")
	public void update(Challenge challenge) throws SQLException
	{
		String challengeSQL = "UPDATE challenges SET regex = ?, name = ? WHERE id = ?";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
		ps.setString(1, challenge.getSampleSolution().getSolution());
		ps.setString(2, challenge.getName());
		ps.setInt(3, challenge.getId());
		ps.execute();
		ps.close();

		_requirements.delete(challenge.getId());
		_requirements.insert(challenge.getRequirements(), challenge.getId());
	}
	
	public void delete(Challenge challenge) throws SQLException
	{
		String challengeSQL = "DELETE FROM challenges WHERE id = ?";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
		ps.setInt(1, challenge.getId());
		ps.execute();
		ps.close();
		
		//requirements are deleted via cascade by the database itself
		//_requirements.delete(challengeId);
	}
}
