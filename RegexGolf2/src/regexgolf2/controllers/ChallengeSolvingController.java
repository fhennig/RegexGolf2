package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.scene.Parent;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Requirement;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.challengesolving.ChallengeSolvingUI;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementItem;
import regexgolf2.ui.subcomponents.solutionediting.TextChangedListener;

public class ChallengeSolvingController
{
	private final ChallengeSolvingUI _ui;
	private SolvableChallenge _challenge;
	
	
	
	//TODO enable challenge == null to disable the UI
	public ChallengeSolvingController(SolvableChallenge challenge)
	{
		try
		{
			_ui = new ChallengeSolvingUI();
		} catch (IOException e)
		{
			// TODO Proper error handling 
			throw new IllegalStateException();
		}
		_challenge = challenge; 
		
		initTextBox();
		initChallengeListener();
		refreshUI();
	}
	
	private void initTextBox()
	{
		_ui.getSolutionEditingUI().addTextChangedListener(new TextChangedListener()
		{
			@Override
			public void textChanged(EventObject event)
			{
				reactToInputChanged();
			}
		});
	}
	
	private void initChallengeListener()
	{
		_challenge.addObjectChangedListener(new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				refreshUI();
			}
		});
	}
	
	private void reactToInputChanged()
	{
		_challenge.getSolution().trySetSolution(_ui.getSolutionEditingUI().getText());
	}
	
	private void refreshUI()
	{
		refreshRequirementUIs();
		refreshScoreDisplay();
		refreshSolutionTextBox();
		refreshChallengeNameLabel();
	}
	
	private void refreshChallengeNameLabel()
	{
		_ui.getChallengeNameLabel().setText(_challenge.getChallenge().getName());
	}
	
	private void refreshRequirementUIs()
	{
		_ui.getMatchRequirementListingUI().setContent(getRequirementItems(true));
		_ui.getNonMatchRequirementListingUI().setContent(getRequirementItems(false));
	}
	
	private void refreshScoreDisplay()
	{
		_ui.getScoreDisplayUI().setAmountCompliedRequirements(_challenge.getAmountCompliedRequirements());
		_ui.getScoreDisplayUI().setAmountRequirements(_challenge.getAmountRequirements());
	}
	
	private void refreshSolutionTextBox()
	{
		_ui.getSolutionEditingUI().setText(_challenge.getSolution().getSolution());
	}
	
	private List<RequirementItem> getRequirementItems(boolean expectedMatchResult)
	{
		List<RequirementItem> result = new ArrayList<RequirementItem>();
		
		for (Requirement r : _challenge.getRequirements(expectedMatchResult))
		{
			result.add(new RequirementItem(r, _challenge.isComplied(r)));
		}
		
		return result;
	}
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
