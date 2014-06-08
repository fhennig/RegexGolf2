package regexgolf2.controllers;

import java.io.IOException;
import java.util.EventObject;

import javafx.scene.control.ListCell;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellListener;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellUI;

import com.google.java.contract.Requires;

public class RequirementCellController
{
	private RequirementCellUI _cellUI;
	private final SolvableChallenge _challenge;
	private Requirement _requirement;
	
	
	
	@Requires("challenge != null")
	public RequirementCellController(SolvableChallenge challenge, boolean editable) throws IOException
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
				reactToChallengeChanged();
			}
		});
	}
	
	private void initCellUI() throws IOException
	{
		_cellUI = new RequirementCellUI(new RequirementCellListener()
		{
			@Override
			public void requirementChanged(Requirement requirement)
			{
				if (_requirement == requirement)
					return;
				if (requirement != null && !_challenge.getChallenge().getRequirements().contains(requirement))
					throw new IllegalArgumentException();

				_requirement = requirement;
				if (_requirement != null)
				{
					_cellUI.setWord(_requirement.getWord());
					_cellUI.setComplied(_challenge.isComplied(_requirement));
				}
			}

			@Override
			public void requirementEdited(String newWord)
			{
				if (_requirement == null)
					throw new IllegalStateException("Requirement cannot be edited, because it is null!");
				_requirement.setWord(newWord);
			}
		});
	}
	
	/**
	 * Refreshes the Word and complied-state of the requirement for this cell
	 */
	private void reactToChallengeChanged()
	{
		if (_requirement == null)
			return; //Do nothing
		_cellUI.setWord(_requirement.getWord());
		_cellUI.setComplied(_challenge.isComplied(_requirement));
	}
	
	public ListCell<Requirement> getCellUI()
	{
		return _cellUI;
	}
}
