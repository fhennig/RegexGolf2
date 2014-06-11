package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.challengerepository.ChallengeRepository;
import regexgolf2.startup.ChallengeFactory;
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
		_challengeRepoController = new ChallengeRepositoryController();
		
		_challengeSolvingController.challengeProperty().bind(_challengeRepoController.selectedChallengeProperty());
		
		_mainUI = new MainUI(_challengeRepoController.getUINode(), null);

				
		Challenge testChallenge = ChallengeFactory.getTestChallenge();
		Solution userSolution = new Solution();
		SolvableChallenge sChallenge = new SolvableChallenge(userSolution, testChallenge);
		
		_mainUI.setMainPaneContent(_challengeSolvingController.getUINode());
	}
	
	
	
	
	@Ensures("result != null")
	public Parent getUINode()
	{
		return _mainUI.getUINode();
	}
	
	//TODO ChallengeRepository.ChallengesChangedEvent
	//TODO challengeRepositoryController (save-, delete-, new-Buttons)
	//TODO challengeSelectionController (Table with Challenges)
	//TODO challengeSolvingController
	//TODO challengeEditingController
}
