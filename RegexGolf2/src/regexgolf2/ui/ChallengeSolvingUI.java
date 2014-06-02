package regexgolf2.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import regexgolf2.model.challenge.Challenge;
import regexgolf2.model.requirement.Requirement;

public class ChallengeSolvingUI
{

    @FXML
    private Label _scoreLabel;

    @FXML
    private TextField _solutionTextField;

    @FXML
    private TableView<Requirement> _dontMatchTableView;

    @FXML
    private TableView<Requirement> _doMatchTableView;
    
    
    private Parent _rootNode;
    
    
    
    public ChallengeSolvingUI(Challenge challenge) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeSolvingUI.fxml")); 
    	loader.setController(this);
    	
    	_rootNode = loader.load();
    	
    	new RequirementListingUI(_doMatchTableView).setContent(challenge.getRequirements(true));
    	new RequirementListingUI(_dontMatchTableView).setContent(challenge.getRequirements(false));
    	new SolutionUI(_solutionTextField).setSolution(challenge.getUserSolution());
    }
    
    public Parent getUI()
    {
    	return _rootNode;
    }

}
