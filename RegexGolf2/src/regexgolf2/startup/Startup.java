package regexgolf2.startup;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import regexgolf2.controllers.ChallengeSolvingController;
import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.Database;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.services.settingsservice.Settings;
import regexgolf2.services.settingsservice.SettingsService;

public class Startup extends Application
{	
	private static Settings _settings;
	
	
	
	
	public static void main(String[] args)
	{
		initSettings();
		initDB();
		
		launch(args);

		//TODO add some general catch for databaseexceptions somewhere
		
		
	}

	@Override
	public void start(Stage stage)
	{
		Challenge testChallenge = ChallengeFactory.getTestChallenge();
		Solution userSolution = new Solution();
		SolvableChallenge sChallenge = new SolvableChallenge(userSolution, testChallenge);
		ChallengeSolvingController controller = new ChallengeSolvingController(sChallenge);
		
		Scene scene = new Scene(controller.getUINode());
		stage.setTitle("Test");
		stage.setWidth(458);
		stage.setHeight(300);
		
		stage.setScene(scene);
		stage.show();
	}
	
	private static void initSettings()
	{
		SettingsService settingsService = new SettingsService(SettingsService.DEFAULT_FILE);
		boolean settingsLoaded = settingsService.tryLoad();
		if (settingsLoaded)
		{
			_settings = settingsService.getSettings();
			return;
		}
		boolean settingsCreated = settingsService.tryCreateDefaultFile();
		if (!settingsCreated)
		{
			JOptionPane.showMessageDialog(null, "Could not find or create a settings.properties File!");
			System.exit(0);
		}
		_settings = settingsService.getSettings();
	}
	
	private static void initDB()
	{
		File dbFile = new File(_settings.getSQLiteDBPath());
		if (!dbFile.exists())
			try
			{
				dbFile.createNewFile();
			} catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Could not find or create a Database File at:\n"+
						_settings.getSQLiteDBPath());
				System.exit(0);
			};
		PersistenceService ps = new PersistenceService(new Database(dbFile));
		testDB(ps);
	}
	
	private static void testDB(PersistenceService ps)
	{
		Challenge c = ChallengeFactory.getIPChallenge();
		ps.getChallengeMapper().getAll();
		ps.getChallengeMapper().insert(c);
		ps.getChallengeMapper().update(c);
		ps.getChallengeMapper().delete(c.getId());
	}
}
