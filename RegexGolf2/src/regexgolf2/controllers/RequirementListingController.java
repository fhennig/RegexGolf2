package regexgolf2.controllers;

import java.util.ArrayList;
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
	
	
	
	public RequirementListingController(boolean expectedMatchResult, boolean editable)
	{
		this(null, expectedMatchResult, editable);
	}
	
	/**
	 * Lists the Requirements with the given expectedMatchResult
	 */
	public RequirementListingController(SolvableChallenge challenge, boolean expectedMatchResult, boolean editable)
	{
		_editable = editable;
		_expectedMatchResult = expectedMatchResult;
		initUI();
		initChallengeListener();
		setChallenge(challenge);
	}
	
	//TODO somehow challenge update has to propagate through to the cells
	
	private void initUI()
	{
		_ui = new RequirementListUI(createRequirementCellFactory(), _editable);
	}
	
	/**
	 * Creates a new RequirementCellHandler that provides interaction logic
	 * for a RequirementCell.
	 */
	private RequirementCellFactory createRequirementCellFactory()
	{
		return new RequirementCellFactory()
		{
			@Override
			public ListCell<Requirement> createCell()
			{
				return new RequirementCellController(_challenge, _editable).getCellUI();
			}
		};
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
				
				refreshRequirementListUI();
			}
		};
	}
	
	private void refreshRequirementListUI()
	{
		List<Requirement> newRequirementList;
		if (_challenge != null)
			newRequirementList = _challenge.getRequirements(_expectedMatchResult);
		else
			newRequirementList = new ArrayList<>();
		
		regexgolf2.util.Util.synchronize(newRequirementList, _ui.getItems());
		
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
			refreshRequirementListUI();
		}
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
