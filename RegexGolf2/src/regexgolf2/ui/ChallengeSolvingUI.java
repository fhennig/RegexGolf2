package regexgolf2.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import regexgolf2.ui.requirementlisting.RequirementItem;
import regexgolf2.ui.requirementlisting.RequirementListingUI;

public class ChallengeSolvingUI
{

    @FXML
    private Label _scoreLabel;
    private ScoreDisplayUI _scoreDisplayUI;

    @FXML
    private TextField _solutionTextField;

    @FXML
    private TableView<RequirementItem> _dontMatchTableView;
    private RequirementListingUI _nonMatchRequirementsUI;

    @FXML
    private TableView<RequirementItem> _doMatchTableView;
    private RequirementListingUI _matchRequirementsUI;
    
    private Parent _rootNode;
    
    
    
    public ChallengeSolvingUI() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeSolvingUI.fxml")); 
    	loader.setController(this);
    	
    	_rootNode = loader.load();
    	
    	_matchRequirementsUI = new RequirementListingUI(_doMatchTableView);
    	_nonMatchRequirementsUI = new RequirementListingUI(_dontMatchTableView);
    	_scoreDisplayUI = new ScoreDisplayUI(_scoreLabel);
    }

    
    
    public RequirementListingUI getNonMatchRequirementListingUI()
    {
    	return _nonMatchRequirementsUI;
    }
    
    public RequirementListingUI getMatchRequirementListingUI()
    {
    	return _matchRequirementsUI;
    }
    
    public ScoreDisplayUI getScoreDisplayUI()
    {
    	return _scoreDisplayUI;
    }
    
    public TextField getSolutionTextField()
    {
    	return _solutionTextField;
    }
    
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
