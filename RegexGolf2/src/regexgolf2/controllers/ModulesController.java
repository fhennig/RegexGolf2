package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Node;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;

import com.google.java.contract.Requires;

public class ModulesController
{
	//TODO UI class


	private final ChallengeRepositoryController _challengeRepoController;
	private final WordRepositoryController _wordRepositoryController;
	//TODO create bindable selectedChallengeProperty
	
	
	
	@Requires({
		"challengeRepository != null",
		"wordRepository != null"
	})
	public ModulesController(ChallengeRepository challengeRepository, WordRepository wordRepository) throws IOException
	{
		_challengeRepoController = new ChallengeRepositoryController(challengeRepository);
		_wordRepositoryController = new WordRepositoryController(wordRepository);
		
		initUI();
	}
	
	
	
	private void initUI()
	{
		//TODO initialize UI
	}
	
	public Node getUINode()
	{
		//TODO call UI class method.
		return null;
	}
}
