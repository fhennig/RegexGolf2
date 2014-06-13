package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;
import regexgolf2.services.persistence.Database;

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
	public List<ChallengeDTO> getAll() throws SQLException
	{
		List<ChallengeDTO> challenges = new ArrayList<>();
		
		String challengeSQL = "SELECT id, regex, name FROM challenges;";
		
		PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
		ResultSet challengeRS = challengePS.executeQuery();
		
		while(challengeRS.next())
		{
			Challenge challenge = new Challenge();
			challenge.getSampleSolution().trySetSolution(challengeRS.getString(2));
			challenge.setName(challengeRS.getString(3));
			ChallengeDTO dto = new ChallengeDTO();
			dto.id = challengeRS.getInt(1);
			dto.challenge = challenge;
			challenges.add(dto);
		}
		challengePS.close();
		
		for (ChallengeDTO dto : challenges)
		{
			//XXX Database requests inside a loop ... is viable with SQLite?
			List<Requirement> cRequirements = _requirements.getAllFor(dto.id);
			for (Requirement r : cRequirements)
				dto.challenge.addRequirement(r);
		}
		return challenges;
	}	
	
	/**
	 * Inserts a Challenge into the database.
	 * The Challenge gets an ID that is written into the given object.
	 * @return the challengeId that was assigned to the challenge
	 * @throws SQLException If initializing failed
	 */
	@Requires("challenge != null")
	public int insert(Challenge challenge) throws SQLException
	{
		int challengeId = getNextChallengeId();
		
		String challengeSQL = "INSERT INTO challenges (id, regex, name) VALUES (?, ?, ?);";
		
		PreparedStatement challengePS = _db.getConnection().prepareStatement(challengeSQL);
		challengePS.setInt(1, challengeId);
		challengePS.setString(2, challenge.getSampleSolution().getSolution());
		challengePS.setString(3, challenge.getName());
		challengePS.execute();
		challengePS.close();

		_requirements.insert(challenge.getRequirements(), challengeId);
		
		return challengeId;
	}
	
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
	public void update(Challenge challenge, int challengeId) throws SQLException
	{
		String challengeSQL = "UPDATE challenges SET regex = ?, name = ? WHERE id = ?";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
		ps.setString(1, challenge.getSampleSolution().getSolution());
		ps.setString(2, challenge.getName());
		ps.setInt(3, challengeId);
		ps.execute();
		ps.close();

		_requirements.delete(challengeId);
		_requirements.insert(challenge.getRequirements(), challengeId);
	}
	
	public void delete(int challengeId) throws SQLException
	{
		String challengeSQL = "DELETE FROM challenges WHERE id = ?";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(challengeSQL);
		ps.setInt(1, challengeId);
		ps.execute();
		
		//requirements are deleted via cascade by the database itself
		//_requirements.delete(challengeId);
	}
	
	public class ChallengeDTO
	{
		public int id;
		public Challenge challenge;
	}
}
