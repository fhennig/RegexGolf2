package regexgolf2.startup;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.ui.ChallengeSolvingUI;

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
		ChallengeSolvingUI ui = null;
		try
		{
			ui = new ChallengeSolvingUI(testChallenge);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scene scene = new Scene(ui.getUI());
		stage.setTitle("Test");
		stage.setWidth(400);
		stage.setHeight(300);
		
		stage.setScene(scene);
		stage.show();
	}
}
