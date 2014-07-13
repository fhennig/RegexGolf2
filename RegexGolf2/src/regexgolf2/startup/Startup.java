package regexgolf2.startup;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import regexgolf2.controllers.MainController;
import regexgolf2.services.services.InitializingException;
import regexgolf2.services.services.Services;
import regexgolf2.util.LogInitializer;

/**
 * This is the class containing the main method. It initializes the Services and
 * the MainController.
 */
public class Startup extends Application
{
	/**
	 * Used to transfer the created Services to the 'start' method.
	 */
	private static Services _services;

	public static void main(String[] args)
	{		
		launch(args);
	}
	
	@Override
	public void init() throws Exception
	{
		LogInitializer.initializeLoggingSettings();
		try
		{
			_services = new Services();
		} catch (InitializingException e)
		{
			Platform.exit();
		}
	}

	@Override
	public void start(Stage primaryStage)
	{
		assert _services != null;

		try
		{
			// Initialize the MainController with the given ChallengeRepository
			// and the primaryStage
			new MainController(_services, primaryStage);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Initializing UI failed!");
			System.exit(0);
		}
	}
}
