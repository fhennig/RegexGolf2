package regexgolf2.model;

import java.util.EventObject;
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
	private ObjectChangedListener _challengeListener;
	private ObjectChangedListener _solutionListener;
	
	
	
	@Requires({
		"solution != null",
		"challenge != null"
	})
	public SolvableChallenge(Solution solution, Challenge challenge)
	{
		_solution = solution;
		_challenge = challenge;
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
				if (!_challenge.equals(event.getSource()))
						throw new IllegalStateException();
				
				reactToSubobjectChange();
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
				
				reactToSubobjectChange();
			}
		};
		_solution.addObjectChangedListener(_solutionListener);
	}
	
	/**
	 * Is used as a reaction to SolutionChanged or ChallengeChanged.
	 */
	private void reactToSubobjectChange()
	{
		refreshComplianceResults();
		fireObjectChangedEvent();
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

	@Requires("_challenge.getRequirements().contains(requirement)")
	public boolean isComplied(Requirement requirement)
	{
		return _complianceResults.get(requirement);
		//throws NPE if the requirement is not in the map
	}
}
