package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.startup.ChallengeFactory;
import regexgolf2.ui.challengerepositoryview.ChallengeRepositoryUI;

public class ChallengeRepositoryController
{
	private final ChallengeRepositoryUI _ui;
	
	
	
	public ChallengeRepositoryController() throws IOException
	{
		_ui = new ChallengeRepositoryUI();
		
		List<SolvableChallenge> list = new ArrayList<>();
		list.add(new SolvableChallenge(new Solution(), ChallengeFactory.getTestChallenge2()));
		_ui.setChallenges(list);
	}
	
	//TODO implement everything
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
