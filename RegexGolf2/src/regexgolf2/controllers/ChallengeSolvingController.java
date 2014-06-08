package regexgolf2.controllers;

import java.io.IOException;
import java.util.EventObject;

import javafx.scene.Parent;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.challengesolving.ChallengeSolvingUI;
import regexgolf2.ui.subcomponents.solutionediting.TextChangedListener;

public class ChallengeSolvingController
{
	private final ChallengeSolvingUI _ui;
	private final RequirementListingController _doMatchController;
	private final RequirementListingController _dontMatchController;
	private SolvableChallenge _challenge;
	
	
	
	//TODO enable challenge == null to disable the UI
	public ChallengeSolvingController(SolvableChallenge challenge)
	{
		_challenge = challenge; 
		_doMatchController = new RequirementListingController(_challenge, true, true);
		_dontMatchController = new RequirementListingController(_challenge, false, true);
		
		try
		{
			_ui = new ChallengeSolvingUI(_doMatchController.getUINode(), _dontMatchController.getUINode());
		} catch (IOException e)
		{
			// TODO Proper error handling 
			throw new IllegalStateException();
		}
		
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
		refreshScoreDisplay();
		refreshSolutionTextBox();
		refreshChallengeNameLabel();
	}
	
	private void refreshChallengeNameLabel()
	{
		_ui.getChallengeNameLabel().setText(_challenge.getChallenge().getName());
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
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
