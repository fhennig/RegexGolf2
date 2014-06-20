package regexgolf2.startup;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

import regexgolf2.controllers.MainController;
import regexgolf2.services.initializing.InitializingService;
import regexgolf2.services.initializing.ServiceContainer;

public class Startup extends Application
{	
	private static ServiceContainer _services;
	
	public static void main(String[] args)
	{
		InitializingService initializer = new InitializingService();
		ServiceContainer sc = initializer.start();
		if (sc == null)
			System.exit(0); //Initialization failed
		
		_services = sc;
		
		launch(args);	
	}

	@Override
	public void start(Stage stage)
	{
		assert _services != null;
		
		
		
		try
		{
			new MainController(_services.getChallengeRepository(), stage);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Initializing UI failed!");
			System.exit(0);
		}
		
//		Scene scene = new Scene(mc.getUINode());
//		stage.setScene(scene);
//		stage.setTitle("RegexGolf");
//		stage.setWidth(_WIDTH);
//		stage.setHeight(_HEIGHT);
//		
//		stage.show();
	}
}
