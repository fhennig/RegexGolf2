package regexgolf2.ui.main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    @FXML
    private Button _wordRepoButton;
    
    private AnchorPane _wordRepositoryPane = new AnchorPane();
    
    private final Parent _rootNode;

    
        
    public MainUI() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _mainPane != null;
    	assert _savedChallengesPane != null;
    	assert _challengeGeneratorPane != null;
    	assert _wordRepoButton != null;
    	
    	initWordRepoButtonHandler();
    }
    
    
    
    private void initWordRepoButtonHandler()
    {
    	_wordRepoButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				Stage stage = new Stage();
				
				stage.setScene(new Scene(_wordRepositoryPane));
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.show();
			}
		});
    }
    
    public void setMainPaneContent(Node content)
    {
    	JavafxUtil.setAsContent(content, _mainPane);
    }
    
    public void setChallengeRepoPanelContent(Node content)
    {
    	JavafxUtil.setAsContent(content, _savedChallengesPane);
    }
    
    public void setChallengeGeneratorPanel(Node content)
    {
    	JavafxUtil.setAsContent(content, _challengeGeneratorPane);
    }
    
    public void setWordRepositoryPanel(Node content)
    {
    	JavafxUtil.setAsContent(content, _wordRepositoryPane);
    }
    
    @Ensures("result != null")
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
