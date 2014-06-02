package regexgolf2.ui.requirementlisting;

import regexgolf2.model.requirement.Requirement;

public class RequirementItem
{
	private final Requirement _requirement;
	private final boolean _isComplied;
	
	
	
	public RequirementItem(Requirement requirement, boolean isComplied)
	{
		_requirement = requirement;
		_isComplied = isComplied;
	}
	
	
	
	public String getWord()
	{
		return _requirement.getWord();
	}
	
	public boolean isComplied()
	{
		return _isComplied;
	}
	
	public Requirement getRequirement()
	{
		return _requirement;
	}
}
