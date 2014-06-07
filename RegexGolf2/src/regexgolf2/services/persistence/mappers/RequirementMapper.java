package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import regexgolf2.model.Requirement;
import regexgolf2.services.persistence.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class RequirementMapper
{
	private final Database _db;
	
	
	
	@Requires("db != null")
	public RequirementMapper(Database db)
	{
		_db = db;
	}
	
	
	
	@Ensures("result != null")
	public List<Requirement> getAllFor(int challengeId) throws SQLException
	{
		List<Requirement> requirements = new ArrayList<>();
		
		String sql = "SELECT expectedMatchResult, word FROM requirements WHERE challenge = ?;";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ps.setInt(1, challengeId);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Requirement r = new Requirement(rs.getBoolean(1), rs.getString(2));
			requirements.add(r);
		}
		
		ps.close();
		return requirements;
	}
	
	@Requires("requirements != null")
	public void insert(Collection<Requirement> requirements, int challengeId) throws SQLException
	{
		String requirementSQL = "INSERT INTO requirements (challenge, expectedMatchResult, word) VALUES (?, ?, ?);";
		
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

	public void delete(int challengeId) throws SQLException
	{
		String sql = "DELETE FROM requirements WHERE challenge = ?";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ps.setInt(1, challengeId);
		ps.execute();
		ps.close();
	}
}
