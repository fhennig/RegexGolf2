package regexgolf2.startup;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import regexgolf2.controllers.ChallengeSolvingController;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.solution.Solution;
import regexgolf2.services.challengesolvingservice.ChallengeSolvingService;
import regexgolf2.services.persistence.ChallengeMapper;
import regexgolf2.services.persistence.Database;
import regexgolf2.services.persistence.IdWrapper;

public class Startup extends Application
{	
	public static void main(String[] args)
	{
		launch(args);
		
		//Test code for database
//		Database db = new Database(new File("C:\\Users\\Felix\\Desktop\\regexgolf.db"));
//		ChallengeMapper mapper = new ChallengeMapper(db);
//		List<IdWrapper<Challenge>> list = mapper.getAll();
//		
//		System.out.println(list.size());
		
		
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
