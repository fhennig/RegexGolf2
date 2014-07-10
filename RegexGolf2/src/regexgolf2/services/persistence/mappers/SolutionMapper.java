package regexgolf2.services.persistence.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import regexgolf2.model.Solution;
import regexgolf2.services.persistence.database.Database;

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
		
		String sql = "SELECT id, challenge, regex FROM solutions;";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			SolutionDTO solution = new SolutionDTO();
			Solution solutionObj = new Solution();
			solution.solution = solutionObj;
			solution.challengeId = rs.getInt(2);
			solutionObj.trySetSolution(rs.getString(3));
			solutions.add(solution);
		}
		ps.close();
		
		return solutions;
	}

	public void insert(Solution solution, int challengeId) throws SQLException
	{
		//solution ID is filled by the autoincrement column
		String sql = "INSERT INTO solutions (challenge, regex) VALUES (?, ?);";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ps.setInt(1, challengeId);
		ps.setString(2, solution.getSolution());
		ps.execute();
		ps.close();
	}
	
	public void update(Solution solution, int challengeId) throws SQLException
	{
		String sql = "UPDATE solutions SET regex = ? WHERE challenge = ?;";
		
		PreparedStatement ps = _db.getConnection().prepareStatement(sql);
		ps.setString(1, solution.getSolution());
		ps.setInt(2, challengeId);
		ps.execute();
		ps.close();
	}
	
	public class SolutionDTO
	{
		public int challengeId;
		public Solution solution;
	}
}
