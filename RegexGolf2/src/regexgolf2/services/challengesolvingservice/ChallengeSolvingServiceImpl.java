package regexgolf2.services.challengesolvingservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.solution.Solution;

import com.google.java.contract.Requires;

public class ChallengeSolvingServiceImpl implements ChallengeSolvingService
{
	private final Challenge _challenge;
	private final Solution _solution;
	private final Map<Requirement, Boolean> _complianceResults = new HashMap<>();
	
	
	@Requires({
		"challenge != null",
		"solution != null"
	})
	public ChallengeSolvingServiceImpl(Challenge challenge, Solution solution)
	{
		_challenge = challenge;
		_solution = solution;
		initMap();
	}
	
	
	
	private void initMap()
	{
		for (Requirement r : _challenge.getRequirements())
		{
			boolean rIsComplied = r.applySolution(_solution);
			_complianceResults.put(r, rIsComplied);
		}
	}
	
	private void refreshComplianceResults()
	{
		boolean complianceResultsChanged = false;
		for (Requirement r : _challenge.getRequirements())
		{
			boolean rIsComplied = r.applySolution(_solution);
			if (_complianceResults.get(r) == rIsComplied)
				continue;
			
			_complianceResults.put(r, rIsComplied);
			complianceResultsChanged = true;
		}
		if (complianceResultsChanged)
			return; //TODO fire event
	}

	@Override
	public void setSolution(String regex)
	{
		boolean solutionChanged = _solution.trySetSolution(regex);
		if (solutionChanged)
			refreshComplianceResults();
	}

	@Override
	public String getSolution()
	{
		return _solution.getSolution();
	}
	
	@Override
	public List<Requirement> getRequirements(boolean expectedMatchresult)
	{
		List<Requirement> result = new ArrayList<>();
		
		for (Requirement r : _challenge.getRequirements())
		{
			if (r.getExpectedMatchResult() == expectedMatchresult)
				result.add(r);
		}
		
		return result;
	}

	@Override
	public int getAmountRequirements()
	{
		return _challenge.getRequirements().size();
	}

	@Override
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



	@Override
	@Requires("_challenge.getRequirements().contains(requirement)")
	public boolean isComplied(Requirement requirement)
	{
		return _complianceResults.get(requirement);
		//throws NPE if the requirement is not in the map
	}
}
