package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import regexgolf2.model.Requirement;

public interface RequirementCellListener
{
	void requirementChanged(Requirement requirement);
	
	void requirementEdited(String newWord);
}
