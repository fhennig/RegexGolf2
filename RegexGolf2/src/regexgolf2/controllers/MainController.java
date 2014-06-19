package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import regexgolf2.services.challengerepository.ChallengeRepository;
import regexgolf2.ui.main.MainUI;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class MainController
{
	private final ChallengeRepository _challengeRepo;

	private final MainUI _mainUI;
	
	private final ChallengeSolvingController _challengeSolvingController;
	private final ChallengeRepositoryController _challengeRepoController;
	
	
	
	@Requires("challengeRepo != null")
	public MainController(ChallengeRepository challengeRepo) throws IOException
	{
		_challengeRepo = challengeRepo;
		_challengeRepo.getAll();

		_challengeSolvingController = new ChallengeSolvingController();
		_challengeRepoController = new ChallengeRepositoryController(_challengeRepo);
		
		_challengeSolvingController.challengeProperty().bind(_challengeRepoController.selectedChallengeProperty());
		_challengeSolvingController.editableProperty().bind(_challengeRepoController.editModeProperty());
		 
		_mainUI = new MainUI();
		_mainUI.setChallengeRepoPanelContent(_challengeRepoController.getUINode());
		
		//Set Challenge Solving UI as Main-Panel content
		_mainUI.setMainPaneContent(_challengeSolvingController.getUINode());
	}
		
	
	
	@Ensures("result != null")
	public Parent getUINode()
	{
		return _mainUI.getUINode();
	}
}
