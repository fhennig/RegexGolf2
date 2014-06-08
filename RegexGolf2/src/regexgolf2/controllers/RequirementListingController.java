package regexgolf2.controllers;

import java.io.IOException;
import java.util.EventObject;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementListUI;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellFactory;

public class RequirementListingController
{
	private RequirementListUI _ui;
	private final boolean _editable;
	private ObjectChangedListener _challengeListener;
	
	private final boolean _expectedMatchResult;
	private SolvableChallenge _challenge;
	
	
	
	/**
	 * Lists the Requirements with the given expectedMatchResult
	 */
	public RequirementListingController(SolvableChallenge challenge, boolean expectedMatchResult, boolean editable)
	{
		_editable = editable;
		initUI();
		_expectedMatchResult = expectedMatchResult;
		initChallengeListener();
		setChallenge(challenge);
	}
	
	
	
	private void initUI()
	{
		_ui = new RequirementListUI(new RequirementCellFactory()
		{
			@Override
			public ListCell<Requirement> createCell()
			{
				try
				{
					return new RequirementCellController(_challenge, _editable).getCellUI();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	private void initChallengeListener()
	{
		_challengeListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				if (!(_challenge != null && _challenge.equals(event.getSource())))
					throw new IllegalStateException();
				
				synchronizeRequirementList();
			}
		};
	}
	
	private void synchronizeRequirementList()
	{
		List<Requirement> newRequirementList = _challenge.getRequirements(_expectedMatchResult);
		
		for (Requirement r : _ui.getItems())
		{
			if (!newRequirementList.contains(r))
				_ui.removeRequirement(r);
		}
		
		for (Requirement r : newRequirementList)
		{
			if (!_ui.getItems().contains(r))
				_ui.addRequirement(r);
		}
		
	}
	
	public void setChallenge(SolvableChallenge challenge)
	{
		if (_challenge != null)
			_challenge.removeObjectChangedListener(_challengeListener);
		_challenge = challenge;
		_ui.clear();
		if (_challenge != null)
		{
			_challenge.addObjectChangedListener(_challengeListener);
			synchronizeRequirementList();
		}
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
