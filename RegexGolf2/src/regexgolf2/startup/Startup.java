package regexgolf2.startup;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import regexgolf2.controllers.MainController;
import regexgolf2.services.initializing.InitializingService;
import regexgolf2.services.initializing.ServiceContainer;

public class Startup extends Application
{	
	private static final int _WIDTH = 800;
	private static final int _HEIGHT = 500;
	
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
		
		MainController mc = null;
		try
		{
			mc = new MainController(_services.getChallengeRepository());
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Initializing UI failed!");
			System.exit(0);
		}
		
		Scene scene = new Scene(mc.getUINode());
		stage.setScene(scene);
		stage.setTitle("RegexGolf");
		stage.setWidth(_WIDTH);
		stage.setHeight(_HEIGHT);
		
		stage.show();
	}
}
