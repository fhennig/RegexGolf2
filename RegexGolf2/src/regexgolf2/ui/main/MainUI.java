package regexgolf2.ui.main;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import regexgolf2.ui.util.JavafxUtil;

import com.google.java.contract.Ensures;

public class MainUI
{
    @FXML
    private AnchorPane _mainPane;
    
    @FXML
    private AnchorPane _savedChallengesPane;

    @FXML
    private AnchorPane _challengeGeneratorPane;
    
    private final Parent _rootNode;

    
    
    public MainUI(Node savedChallengesUI, Node challengeGeneratorUI) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _mainPane != null;
    	assert _savedChallengesPane != null;
    	assert _challengeGeneratorPane != null;
    	
    	JavafxUtil.setAsContent(savedChallengesUI, _savedChallengesPane);
    	JavafxUtil.setAsContent(challengeGeneratorUI, _challengeGeneratorPane);
    }
    
    
    
    public void setMainPaneContent(Node content)
    {
    	JavafxUtil.setAsContent(content, _mainPane);
    }
    
    @Ensures("result != null")
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
