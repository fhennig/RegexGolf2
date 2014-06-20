package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.stage.Stage;
import regexgolf2.services.challengerepository.ChallengeRepository;
import regexgolf2.ui.main.MainUI;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * The root controller that connects the main application parts together
 * and initializes the main UI.
 */
public class MainController
{
	private final ChallengeRepository _challengeRepo;

	private MainUI _mainUI;
	
	private final ChallengeSolvingController _challengeSolvingController;
	private final ChallengeRepositoryController _challengeRepoController;
	private final WordRepositoryController _wordRepositoryController;
	
	
	
	/**
	 * @param challengeRepo The ChallengeRepository that will be used
	 * @param primaryStage  The main stage of the application, where the main UI will be placed.
	 * @throws IOException  if initializing the UI components failed (fxml document failed to load).
	 */
	@Requires("challengeRepo != null")
	public MainController(ChallengeRepository challengeRepo, Stage primaryStage) throws IOException
	{
		_challengeRepo = challengeRepo;
		_challengeRepo.getAll();
		
		_wordRepositoryController = new WordRepositoryController(); //TODO initialize properly

		_challengeSolvingController = new ChallengeSolvingController();
		_challengeRepoController = new ChallengeRepositoryController(_challengeRepo);
		
		_challengeSolvingController.challengeProperty().bind(_challengeRepoController.selectedChallengeProperty());
		_challengeSolvingController.editableProperty().bind(_challengeRepoController.editModeProperty());
		 
		initUI(primaryStage);
	}
		
	
	
	private void initUI(Stage stage) throws IOException
	{
		_mainUI = new MainUI(stage);
		
		//Insert Child UI elements:
		//ChallengeRepository
		_mainUI.setChallengeRepoPanelContent(_challengeRepoController.getUINode());
		//ChallengeSolvingUI into mainPane
		_mainUI.setMainPaneContent(_challengeSolvingController.getUINode());
		//Word repository Panel
		_mainUI.setWordRepositoryPanel(_wordRepositoryController.getUINode());
		
		//TODO challengegenerator
	}
	
	@Ensures("result != null")
	public Parent getUINode()
	{
		return _mainUI.getUINode();
	}
}
