package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Solution;
import regexgolf2.services.persistence.Database;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class SolutionMapper
{
	private final Database _db;
	
	
	
	@Requires("db != null")
	public SolutionMapper(Database db)
	{
		_db = db;
	}
	
	
	
	@Ensures("result != null")
	public List<Solution> getAll() throws SQLException
	{
		List<Solution> solutions = new ArrayList<>();
		
		String sql = "SELECT id, challenge, regex FROM solutions";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Solution solution = new Solution();
			solution.setId(rs.getInt(1));
			//TODO handle challengeId
			solution.trySetSolution(rs.getString(3));
			
		}
		ps.close();
		
		return solutions;
	}
}
