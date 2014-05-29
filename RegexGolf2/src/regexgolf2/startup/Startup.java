package regexgolf2.startup;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.solution.Solution;
import regexgolf2.ui.RequirementListingUI;

public class Startup extends Application
{
	private Button _button;
	private Challenge _challenge;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	private void initChallenge()
	{
		_challenge = new Challenge(new Solution(""), new Solution(""));
		_challenge.addRequirement(new Requirement(true, "Haus"));
		_challenge.addRequirement(new Requirement(true, "Auto"));
	}

	@Override
	public void start(Stage stage)
	{
		initChallenge();
		initButton();
		
		VBox vb = new VBox();
		Scene scene = new Scene(vb);
		stage.setTitle("Test");
		stage.setWidth(200);
		stage.setHeight(300);
		
		RequirementListingUI ui = new RequirementListingUI();
		ui.setEditable(true);
		vb.getChildren().add(ui.getControl());
		vb.getChildren().add(_button);
		ui.setContent(_challenge.getRequirements());
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	private void initButton()
	{
		_button = new Button("Press me");
		_button.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				_challenge.getUserSolution().setSolution("Auto");
			}
		});
	}
}
