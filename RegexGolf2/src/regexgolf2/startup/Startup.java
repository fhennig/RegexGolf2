package regexgolf2.startup;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import regexgolf2.controllers.ChallengeSolvingController;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.solution.Solution;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingService;

public class Startup extends Application
{	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		Challenge testChallenge = ChallengeFactory.getTestChallenge();
		Solution userSolution = new Solution();
		ChallengeSolvingService service = new ChallengeSolvingService(testChallenge, userSolution);
		ChallengeSolvingController controller = new ChallengeSolvingController(service);
		
		Scene scene = new Scene(controller.getUINode());
		stage.setTitle("Test");
		stage.setWidth(400);
		stage.setHeight(300);
		
		stage.setScene(scene);
		stage.show();
	}
}
