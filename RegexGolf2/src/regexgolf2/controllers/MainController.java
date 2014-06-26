package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.stage.Stage;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.ui.main.MainUI;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * The root controller that connects the main application parts together
 * and initializes the main UI.
 */
public class MainController
{
	private MainUI _mainUI;
	
	private final ChallengeSolvingController _challengeSolvingController;
	private final ModulesController _modulesController;
	
	
	
	/**
	 * @param challengeRepo The ChallengeRepository that will be used
	 * @param primaryStage  The main stage of the application, where the main UI will be placed.
	 * @throws IOException  if initializing the UI components failed (fxml document failed to load).
	 */
	@Requires("challengeRepo != null")
	public MainController(ChallengeRepository challengeRepo, WordRepository wordRepo, Stage primaryStage) throws IOException
	{
		_challengeSolvingController = new ChallengeSolvingController();
		_modulesController = new ModulesController(challengeRepo, wordRepo, primaryStage);
		
		_challengeSolvingController.challengeProperty().bind(_modulesController.challengeProperty());
		
		//TODO get bindable editableProperty somehow
		//_challengeSolvingController.editableProperty().bind(_challengeRepoController.editModeProperty());
		 
		initUI(primaryStage);
	}
		
	
	
	private void initUI(Stage stage) throws IOException
	{
		_mainUI = new MainUI(stage);
		
		_mainUI.setMainPaneContent(_challengeSolvingController.getUINode());
		_mainUI.setModulesPaneContent(_modulesController.getUINode());
	}
	
	@Ensures("result != null")
	public Parent getUINode()
	{
		return _mainUI.getUINode();
	}
}
