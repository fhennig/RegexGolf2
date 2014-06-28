package regexgolf2.ui.challengegenerator;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ChallengeGeneratorUI
{
    @FXML
    private Button _generateButton;
    
    private Node _rootNode;
    
    
    
    public ChallengeGeneratorUI() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeGeneratorUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _generateButton != null;
    }
    
    
	
    public Button getGenerateButton()
    {
    	return _generateButton;
    }
    
	public Node getUINode()
	{
		return _rootNode;
	}
}
