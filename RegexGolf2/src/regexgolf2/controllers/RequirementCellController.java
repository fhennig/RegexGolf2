package regexgolf2.controllers;

import java.util.EventObject;

import javafx.scene.control.ListCell;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellHandler;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellUI;

import com.google.java.contract.Requires;

public class RequirementCellController
{
	private RequirementCellUI _cellUI;
	private final SolvableChallenge _challenge;
	
	
	
	@Requires("challenge != null")
	public RequirementCellController(SolvableChallenge challenge, boolean editable)
	{
		_challenge = challenge;
		initChallengeListener();
		initCellUI();
		_cellUI.setIsEditable(editable);
	}

	
	private void initChallengeListener()
	{
		_challenge.addObjectChangedListener(new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				refreshCellUI();
			}
		});
	}
	
	private void initCellUI()
	{
		_cellUI = new RequirementCellUI(new RequirementCellHandler()
		{
			@Override
			public void requirementChanged(RequirementCellUI source,
					Requirement requirement)
			{
				if (_challenge == null || !_challenge.getChallenge().getRequirements().contains(requirement))
					throw new IllegalArgumentException();
				refreshCellUI();
			}
	
			@Override
			public void requirementEdited(RequirementCellUI source,
					String newWord)
			{
				if (source.getRequirement() == null)
					throw new IllegalStateException("Requirement cannot be edited, because it is null!");
				source.getRequirement().setWord(newWord);
			}
		});
	}
	
	/**
	 * Refreshes the Word and complied-state of the requirement for this cell
	 */
	private void refreshCellUI()
	{
		Requirement r = _cellUI.getRequirement();
		if (r == null)
			return;
		_cellUI.setWord(r.getWord());
		_cellUI.setComplied(_challenge.isComplied(r));
	}
	
	public ListCell<Requirement> getCellUI()
	{
		return _cellUI;
	}
}
