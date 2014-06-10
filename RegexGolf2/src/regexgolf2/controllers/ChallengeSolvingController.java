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
	private ObjectChangedListener _challengeListener;
	
	
	
	public ChallengeSolvingController() throws IOException
	{
		this(null);
	}
	
	public ChallengeSolvingController(SolvableChallenge challenge) throws IOException
	{
		_challenge = challenge; 
		_doMatchController = new RequirementListingController(_challenge, true, false);
		_dontMatchController = new RequirementListingController(_challenge, false, false);

		_ui = new ChallengeSolvingUI(_doMatchController.getUINode(), _dontMatchController.getUINode());

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
		_challengeListener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				refreshUI();
			}
		};
	}
	
	private void reactToInputChanged()
	{
		if (_challenge == null)
			return;
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
		String text = (_challenge == null ? "" : _challenge.getChallenge().getName());
		_ui.getChallengeNameLabel().setText(text);
	}
	
	private void refreshScoreDisplay()
	{
		if (_challenge == null)
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(0);
			_ui.getScoreDisplayUI().setAmountRequirements(0);
			_ui.getScoreDisplayUI().setHighlight(false);
		}
		else
		{
			_ui.getScoreDisplayUI().setAmountCompliedRequirements(_challenge.getAmountCompliedRequirements());
			_ui.getScoreDisplayUI().setAmountRequirements(_challenge.getAmountRequirements());
			_ui.getScoreDisplayUI().setHighlight(_challenge.isSolved());
		}
	}
	
	private void refreshSolutionTextBox()
	{
		String text = (_challenge == null ? "" : _challenge.getSolution().getSolution());
		_ui.getSolutionEditingUI().setText(text);
	}
	
	public void setChallenge(SolvableChallenge challenge)
	{
		if (_challenge == challenge)
			return; //Nothing to do here
		if (_challenge != null)
			_challenge.removeObjectChangedListener(_challengeListener);
		_challenge = challenge;
		if (_challenge != null)
			_challenge.addObjectChangedListener(_challengeListener);
		
		_doMatchController.setChallenge(_challenge);
		_dontMatchController.setChallenge(_challenge);
	}
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
