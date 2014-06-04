package regexgolf2.ui.challengesolving;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementItem;
import regexgolf2.ui.subcomponents.requirementlisting.RequirementListingUI;
import regexgolf2.ui.subcomponents.scoredisplay.ScoreDisplayUI;
import regexgolf2.ui.subcomponents.solutionediting.SolutionEditingUI;

public class ChallengeSolvingUI
{
    @FXML
    private Label _challengeNameLabel;

    @FXML
    private Label _scoreLabel;
    private ScoreDisplayUI _scoreDisplayUI;

    @FXML
    private TextField _solutionTextField;
    private SolutionEditingUI _solutionEditingUI;

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
    	_solutionEditingUI = new SolutionEditingUI(_solutionTextField);
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
    
    public SolutionEditingUI getSolutionEditingUI()
    {
    	return _solutionEditingUI;
    }
    
    public Label getChallengeNameLabel()
    {
    	return _challengeNameLabel;
    }
    
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
