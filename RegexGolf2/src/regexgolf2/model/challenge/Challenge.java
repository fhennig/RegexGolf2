package regexgolf2.model.challenge;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.ObservableObject;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.solution.Solution;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;


public class Challenge extends ObservableObject
{
	private final Set<Requirement> _requirements = new HashSet<>();
	private Solution _sampleSolution;
	private String _name = "";
	
	private ObjectChangedListener _requirementListener;
	
	
	
	public Challenge()
	{
		_sampleSolution = new Solution();
		initRequirementListener();
		initSampleSolutionListener();
	}
	
	
	
	private void initRequirementListener()
	{
		_requirementListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				Object source = event.getSource();
				
				if (!_requirements.contains(source))
					throw new IllegalStateException(
							"The eventsource is not a Requirement that is owned " +
							"by this challenge, this listener should not be called!");
				
				fireObjectChangedEvent();
			}
		};
	}
	
	private void initSampleSolutionListener()
	{
		_sampleSolution.addObjectChangedListener(new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				fireObjectChangedEvent();
			}
		});
	}
	
	@Ensures("result != null")
	public String getName()
	{
		return _name;
	}
	
	@Requires("name != null")
	public void setName(String name)
	{
		if (_name.equals(name))
			return;
		_name = name;
		fireObjectChangedEvent();
	}
	
	@Ensures("result != null")
	public Solution getSampleSolution()
	{
		return _sampleSolution;
	}
	
	@Requires("requirement != null")
	public void addRequirement(Requirement requirement)
	{
		boolean elementWasAdded = _requirements.add(requirement);
		if (!elementWasAdded)
			return;
		fireObjectChangedEvent();
		requirement.addObjectChangedListener(_requirementListener);
	}
	
	@Requires("requirement != null")
	public void removeRequirement(Requirement requirement)
	{
		boolean wasRemoved = _requirements.remove(requirement);
		if (wasRemoved)
			requirement.removeObjectChangedListener(_requirementListener);
	}

	/**
	 * Returns an unmodifiable View of the internal Set
	 */
	@Ensures("result != null")
	public Set<Requirement> getRequirements()
	{
		return Collections.unmodifiableSet(_requirements);
	}
	
	public int getAmountRequirements()
	{
		return _requirements.size();
	}
}
