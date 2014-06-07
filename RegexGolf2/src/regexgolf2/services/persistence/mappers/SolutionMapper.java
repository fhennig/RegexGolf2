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
	public List<SolutionDTO> getAll() throws SQLException
	{
		List<SolutionDTO> solutions = new ArrayList<>();
		
		String sql = "SELECT id, challenge, regex FROM solutions";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			SolutionDTO solution = new SolutionDTO();
			Solution solutionObj = new Solution();
			solution.solution = solutionObj;
			solutionObj.setId(rs.getInt(1));
			solution.challengeId = rs.getInt(2);
			solutionObj.trySetSolution(rs.getString(3));
			solutions.add(solution);
		}
		ps.close();
		
		return solutions;
	}

	public void insert(Solution solution, int challengeId)
	{
		
	}
	
	public class SolutionDTO
	{
		public int challengeId;
		public Solution solution;
	}
}
