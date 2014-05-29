package regexgolf2.model.challenge;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.requirement.WordChangedListener;
import regexgolf2.model.solution.Solution;
import regexgolf2.model.solution.SolutionChangedListener;
import regexgolf2.services.challengerepository.TrackableObject;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;


public class Challenge extends TrackableObject
{
	private final List<RequirementsChangedListener> _requirementsChangedListeners = new ArrayList<>();
	private final List<Requirement> _requirements = new ArrayList<>();
	private Solution _userSolution;
	private Solution _sampleSolution;
	
	
	
	@Requires("solution != null")
	public Challenge(Solution userSolution, Solution sampleSolution)
	{
		_userSolution = userSolution;
		_sampleSolution = sampleSolution;
		registerUserSolutionChangedListener();
		registerSampleSolutionListener();
	}
	
	
	
	@Ensures("result != null")
	public Solution getUserSolution()
	{
		return _userSolution;
	}
	
	@Ensures("result != null")
	public Solution getSampleSolution()
	{
		return _sampleSolution;
	}
	
	@Requires("requirement != null")
	public void addRequirement(Requirement requirement)
	{
		_requirements.add(requirement);
		registerWordChangedListener(requirement);
		fireRequirementsChangedEvent();
		fireObjectChangedEvent();
	}
	
	@Requires("requirement != null")
	public void removeRequirement(Requirement requirement)
	{
		_requirements.remove(requirement);
		fireRequirementsChangedEvent();
		fireObjectChangedEvent();
	}

	/**
	 * Returns an editable Copy of the List of Requirements
	 */
	public List<Requirement> getRequirements()
	{
		return new ArrayList<Requirement>(_requirements);
	}
	
	private void registerWordChangedListener(Requirement requirement)
	{
		requirement.addWordChangedListener(new WordChangedListener()
		{
			@Override
			public void wordChanged(EventObject event)
			{
				Requirement requirement = getRequirement(event.getSource());
				if (requirement == null)
					throw new IllegalArgumentException();
				
				requirement.applySolution(getUserSolution());
				fireObjectChangedEvent();
			}
		});
	}
	
	private void registerUserSolutionChangedListener()
	{
		_userSolution.addSolutionChangedListener(new SolutionChangedListener()
		{
			@Override
			public void solutionChanged(EventObject event)
			{
				if (event.getSource() != _userSolution)
					throw new IllegalArgumentException(
							"SolutionChangedEvent Source was not the object that was subscribed to!");
				
				refreshRequirementMatchResults();
				fireObjectChangedEvent();
			}
		});
	}
	
	private void registerSampleSolutionListener()
	{
		_sampleSolution.addSolutionChangedListener(new SolutionChangedListener()
		{
			@Override
			public void solutionChanged(EventObject event)
			{
				if (event.getSource() != _sampleSolution)
					throw new IllegalArgumentException();
				
				fireObjectChangedEvent();
			}
		});
	}
	
	private void refreshRequirementMatchResults()
	{
		for (Requirement r : _requirements)
		{
			r.applySolution(getUserSolution());
		}
	}
	
	/**
	 * Tries to find the requirement matching the given Object
	 * @param other The Object that should be matched
	 * @return the Requirement matching the given Object,
	 * 		   or null if matching Requirement was not found.
	 */
	@Requires("other != null")
	private Requirement getRequirement(Object other)
	{
		for (Requirement r : _requirements)
		{
			if (other == r)
			{
				return r;
			}
		}
		return null;
	}
	
	private void fireRequirementsChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (RequirementsChangedListener listener : _requirementsChangedListeners)
		{
			listener.requirementsChanged(event);
		}
	}
	
	/**
	 * Gets called if Requirements are added or removed.
	 * @param listener The listener that wants to be notified
	 */
	@Requires("listener != null")
	public void addRequirementsChangedListener(RequirementsChangedListener listener)
	{
		_requirementsChangedListeners.add(listener);
	}
}
