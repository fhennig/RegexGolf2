package regexgolf2.startup;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import regexgolf2.controllers.ChallengeSolvingController;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingService;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingServiceImpl;

public class Startup extends Application
{	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		Challenge testChallenge = ChallengeFactory.getIPChallenge();
		ChallengeSolvingService service = new ChallengeSolvingServiceImpl(testChallenge, testChallenge.getUserSolution());
		ChallengeSolvingController controller = new ChallengeSolvingController(service);
		
		Scene scene = new Scene(controller.getUINode());
		stage.setTitle("Test");
		stage.setWidth(400);
		stage.setHeight(300);
		
		stage.setScene(scene);
		stage.show();
	}
}
