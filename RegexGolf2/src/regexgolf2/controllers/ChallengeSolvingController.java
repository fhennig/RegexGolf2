package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.scene.Parent;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.services.ServiceChangedListener;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingService;
import regexgolf2.ui.challengesolving.ChallengeSolvingUI;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementItem;
import regexgolf2.ui.subcomponents.solutionediting.TextChangedListener;

public class ChallengeSolvingController
{
	private final ChallengeSolvingUI _ui;
	private final ChallengeSolvingService _service;
	
	
	
	public ChallengeSolvingController(ChallengeSolvingService challengeSolvingService)
	{
		try
		{
			_ui = new ChallengeSolvingUI();
		} catch (IOException e)
		{
			// TODO Proper error handling 
			throw new IllegalStateException();
		}
		_service = challengeSolvingService;
		
		initTextBox();
		initServiceListener();
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
	
	private void initServiceListener()
	{
		_service.addServiceChangedListener(new ServiceChangedListener()
		{
			@Override
			public void serviceChanged(EventObject event)
			{
				reactToServiceChanged();
			}
		});
	}
	
	private void reactToServiceChanged()
	{
		refreshUI();
	}
	
	private void reactToInputChanged()
	{
		_service.getSolution().trySetSolution(_ui.getSolutionEditingUI().getText());
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
		_ui.getChallengeNameLabel().setText(_service.getChallenge().getName());
	}
	
	private void refreshRequirementUIs()
	{
		_ui.getMatchRequirementListingUI().setContent(getRequirementItems(true));
		_ui.getNonMatchRequirementListingUI().setContent(getRequirementItems(false));
	}
	
	private void refreshScoreDisplay()
	{
		_ui.getScoreDisplayUI().setAmountCompliedRequirements(_service.getAmountCompliedRequirements());
		_ui.getScoreDisplayUI().setAmountRequirements(_service.getAmountRequirements());
	}
	
	private void refreshSolutionTextBox()
	{
		_ui.getSolutionEditingUI().setText(_service.getSolution().getSolution());
	}
	
	private List<RequirementItem> getRequirementItems(boolean expectedMatchResult)
	{
		List<RequirementItem> result = new ArrayList<RequirementItem>();
		
		for (Requirement r : _service.getRequirements(expectedMatchResult))
		{
			result.add(new RequirementItem(r, _service.isComplied(r)));
		}
		
		return result;
	}
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
