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
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private Stage _wordRepositoryStage;
    
    private final Parent _rootNode;

    
        
    public MainUI(final Stage stage) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _mainPane != null;
    	assert _savedChallengesPane != null;
    	assert _challengeGeneratorPane != null;
    	assert _wordRepoButton != null;
    	
    	initWordRepoButtonHandler(stage);
    	
    	Scene mainScene = new Scene(_rootNode);
    	stage.setScene(mainScene);
    	stage.setTitle("RegexGolf");
    	stage.setWidth(800);
    	stage.setHeight(500);

    	stage.show();
    }
    
    
    
    private void initWordRepoStage(Window parent)
    {
    	_wordRepositoryStage = new Stage();
    	
    	_wordRepositoryStage.setScene(new Scene(_wordRepositoryPane));
    	//Set dimensions:
    	_wordRepositoryStage.setWidth(300);
    	_wordRepositoryStage.setHeight(500);
    	//Center stage in parentstage
    	_wordRepositoryStage.setX(
    			parent.getX() + parent.getWidth() / 2
    			- _wordRepositoryStage.getWidth() / 2);
    	_wordRepositoryStage.setY(
    			parent.getY() + parent.getHeight() / 2
    			- _wordRepositoryStage.getWidth() / 2);
    	_wordRepositoryStage.setY(parent.getHeight() / 2);
    }
    
    /**
     * Initializes the WordRepository-Button Handler.
     * The Handler initializes the WordRepositoryStage if it is not initialized
     * and show it and focuses it.
     */
    private void initWordRepoButtonHandler(Window dialogParent)
    {
    	final Window parent = dialogParent;
    	_wordRepoButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				//lazy initialization
				if (_wordRepositoryStage == null)
					initWordRepoStage(parent);
				_wordRepositoryStage.show();
				_wordRepositoryStage.requestFocus();
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
