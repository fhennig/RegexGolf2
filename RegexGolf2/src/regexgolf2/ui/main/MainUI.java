package regexgolf2.ui.main;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    	stage.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean newValue)
			{
				if (!newValue);
//					stage.toFront();
			}
    		
		});
    	stage.show();
    }
    
    
    
    private void initWordRepoStage(Window parent)
    {
    	_wordRepositoryStage = new Stage();
    	
    	_wordRepositoryStage.setScene(new Scene(_wordRepositoryPane));
//    	_wordRepositoryStage.initModality(Modality.APPLICATION_MODAL);
//    	_wordRepositoryStage.initOwner(parent);
    	//Set dimensions:
    	_wordRepositoryStage.setWidth(500);
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
    
    private void initWordRepoButtonHandler(Window dialogParent)
    {
    	final Window parent = dialogParent;
    	_wordRepoButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				if (_wordRepositoryStage == null)
					initWordRepoStage(parent);
				_wordRepositoryStage.show();
//				_wordRepositoryStage.requestFocus();
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
