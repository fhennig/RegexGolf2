package regexgolf2.services.challengesolvingservice;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.solution.Solution;
import regexgolf2.services.ObservableService;

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

@Invariant({
	"_challenge != null",
	"_solution != null"
})
public class ChallengeSolvingService extends ObservableService
{
	private Challenge _challenge;
	private Solution _solution;
	private final Map<Requirement, Boolean> _complianceResults = new HashMap<>();
	private ObjectChangedListener _challengeListener;
	private ObjectChangedListener _solutionListener;
	
	
	
	@Requires({
		"challenge != null",
		"solution != null"
	})
	public ChallengeSolvingService(Challenge challenge, Solution solution)
	{
		_challenge = challenge;
		_solution = solution;
		refreshComplianceResults();
		initListeners();
	}
	
	
	
	private void initListeners()
	{
		_challengeListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (!_solution.equals(event.getSource()))
						throw new IllegalStateException();
				
				reactToChallengeChange();
			}
		};
		_challenge.addObjectChangedListener(_challengeListener);
		
		_solutionListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (!_solution.equals(event.getSource()))
						throw new IllegalStateException();
				
				reactToSolutionChange();
			}
		};
		_solution.addObjectChangedListener(_solutionListener);
	}
	
	private void reactToChallengeChange()
	{
		refreshComplianceResults();
		fireServiceChangedEvent();
	}
	
	private void reactToSolutionChange()
	{
		refreshComplianceResults();
		fireServiceChangedEvent();
	}
	
	private void refreshComplianceResults()
	{
		_complianceResults.clear();
		for (Requirement r : _challenge.getRequirements())
		{
			boolean rIsComplied = r.applySolution(_solution);			
			_complianceResults.put(r, rIsComplied);
		}
	}

	@Requires("solution != null")
	public void setSolution(Solution solution)
	{
		_solution.removeObjectChangedListener(_solutionListener);
		_solution = solution;
		_solution.addObjectChangedListener(_solutionListener);
		refreshComplianceResults();
		fireServiceChangedEvent();
	}
	
	@Ensures("result != null")
	public Solution getSolution()
	{
		return _solution;
	}
	
	/**
	 * Helper method to get filtered requirements
	 * @param expectedMatchresult The value that should be filtered with
	 */
	@Ensures("result != null")
	public List<Requirement> getRequirements(boolean expectedMatchresult)
	{
		return _challenge.getRequirements(expectedMatchresult);
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

	@Requires("_challenge.getRequirements().contains(requirement)")
	public boolean isComplied(Requirement requirement)
	{
		return _complianceResults.get(requirement);
		//throws NPE if the requirement is not in the map
	}
	
	@Requires("challenge != null")
	@Ensures("getChallenge() == challenge")
	public void setChallenge(Challenge challenge)
	{
		_challenge.removeObjectChangedListener(_challengeListener);
		_challenge = challenge;
		_challenge.addObjectChangedListener(_challengeListener);
		refreshComplianceResults();
		fireServiceChangedEvent();
	}
	
	@Ensures("result != null")
	public Challenge getChallenge()
	{
		return _challenge;
	}
}
