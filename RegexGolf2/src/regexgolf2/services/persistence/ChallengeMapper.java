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
	private final Database _db;
	
	
	
	public ChallengeMapper(Database db)
	{
		_db = db;
	}
	
	
	
	public List<IdWrapper<Challenge>> getAll()
	{
		List<IdWrapper<Challenge>> challenges = new ArrayList<>();
		try
		{
			PreparedStatement challengePS = _db.getConnection().prepareStatement(
					"SELECT c.id, c.name, s.regex FROM challenges c, solutions s WHERE c.solution = s.id");
			ResultSet challengeRS = challengePS.executeQuery();
			
			while(challengeRS.next())
			{
				Challenge challenge = new Challenge();
				challenge.setName(challengeRS.getString(2));
				challenge.getSampleSolution().trySetSolution(challengeRS.getString(3));
				IdWrapper<Challenge> wrapper = new IdWrapper<Challenge>(challengeRS.getInt(1), challenge);
				challenges.add(wrapper);
			}
			
			PreparedStatement requirementPS = _db.getConnection().prepareStatement(
					"SELECT challenge, expectedMatchResult, word FROM requirements");
			ResultSet requirementRS = requirementPS.executeQuery();
			
			while(requirementRS.next())
			{
				Requirement requirement = new Requirement(
						requirementRS.getBoolean(2), requirementRS.getString(3));
				int challengeId = requirementRS.getInt(1);
				
				for (IdWrapper<Challenge> wrapper : challenges)
				{
					if (wrapper.getId() == challengeId)
					{
						wrapper.getItem().addRequirement(requirement);
					}
				}
			}
		}
		catch (SQLException e)
		{
			throw new DatabaseErrorException(e);
		}
		return challenges;
	}	
}
