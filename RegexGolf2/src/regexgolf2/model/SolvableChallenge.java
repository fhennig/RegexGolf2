package regexgolf2.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class SolvableChallenge extends ObservableObject
{
	private final Solution _solution;
	private final Challenge _challenge;
	private final Map<Requirement, Boolean> _complianceResults = new HashMap<>();
	private final ScoreCalculator _scoreCalc;
	
	
	
	public SolvableChallenge()
	{
		this(new Solution(), new Challenge());
	}
	
	@Requires({
		"solution != null",
		"challenge != null"
	})
	public SolvableChallenge(Solution solution, Challenge challenge)
	{
		_solution = solution;
		_challenge = challenge;
		refreshComplianceResults();
		_scoreCalc = new ScoreCalculator(this);
		_challenge.addObjectChangedListener(e -> subobjectChanged());
		_solution.addObjectChangedListener(e -> subobjectChanged());
	}
	
		
	
	/**
	 * Is used as a reaction to SolutionChanged or ChallengeChanged.
	 */
	private void subobjectChanged()
	{
		refreshComplianceResults();
		fireObjectChangedEvent();
	}
	
	private void refreshComplianceResults()
	{
		_complianceResults.clear();
		for (Requirement r : _challenge.getRequirements())
		{
			boolean rIsComplied = r.applySolution(getSolution());			
			_complianceResults.put(r, rIsComplied);
		}
	}
	
	@Ensures("result != null")
	public Solution getSolution()
	{
		return _solution;
	}
	
	@Ensures("result != null")
	public Challenge getChallenge()
	{
		return _challenge;
	}
	
	public List<Requirement> getRequirements(boolean expectedMatchResult)
	{
		return _challenge.getRequirements(expectedMatchResult);
	}
	
	public int getAmountRequirements()
	{
		return _challenge.getAmountRequirements();
	}

	public int getAmountCompliedRequirements()
	{
		int compliedRequirements = 0;
		for (Requirement r : _challenge.getRequirements())
		{
			if (_complianceResults.get(r) == true)
				compliedRequirements++;
		}
		return compliedRequirements;
	}
	
	public int getScore()
	{
		return _scoreCalc.getScore();
	}
	
	public boolean isSolved()
	{
		for (Requirement r : _challenge.getRequirements())
		{
			if (_complianceResults.get(r) == false)
				return false;
		}
		return true;
	}

	@Requires("getChallenge().getRequirements().contains(requirement)")
	public boolean isComplied(Requirement requirement)
	{
		try
		{
			return _complianceResults.get(requirement);
		}
		catch (NullPointerException npe)
		{
			throw new IllegalArgumentException("Requirement: " + requirement +
					" is not a Requirement of this challenge (" + this + ")");
		}
	}
	
	@Override
	public String toString()
	{
		return _challenge.getName();
	}
}
