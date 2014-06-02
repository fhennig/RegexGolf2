package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingService;
import regexgolf2.ui.ChallengeSolvingUI;
import regexgolf2.ui.requirementlisting.RequirementItem;

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
		
		init();
	}
	
	private void init()
	{
		_ui.getMatchRequirementListingUI().setContent(getRequirementItems(true));
		_ui.getNonMatchRequirementListingUI().setContent(getRequirementItems(false));
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
