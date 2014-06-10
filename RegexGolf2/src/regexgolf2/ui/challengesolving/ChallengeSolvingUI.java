package regexgolf2.ui.challengesolving;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import regexgolf2.ui.subcomponents.scoredisplay.ScoreDisplayUI;
import regexgolf2.ui.subcomponents.solutionediting.SolutionEditingUI;
import regexgolf2.ui.util.JavafxUtil;

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
    private AnchorPane _doMatchPane;

    @FXML
    private AnchorPane _dontMatchPane;
    
    private Parent _rootNode;
    
    
    
    public ChallengeSolvingUI(Node doMatchUI, Node dontMatchUI) throws IOException
    {    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeSolvingUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _challengeNameLabel != null;
    	assert _scoreLabel != null;
    	assert _solutionTextField != null;
    	assert _doMatchPane != null;
    	assert _dontMatchPane != null;

    	JavafxUtil.setAsContent(doMatchUI, _doMatchPane);
    	JavafxUtil.setAsContent(dontMatchUI, _dontMatchPane);
    	
    	_scoreDisplayUI = new ScoreDisplayUI(_scoreLabel);
    	_solutionEditingUI = new SolutionEditingUI(_solutionTextField);
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
