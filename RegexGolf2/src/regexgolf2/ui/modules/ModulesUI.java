package regexgolf2.ui.modules;

import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import regexgolf2.ui.util.JavafxUtil;

import com.google.java.contract.Ensures;

public class ModulesUI
{
    @FXML
    private AnchorPane _savedChallengesPane;

    @FXML
    private AnchorPane _challengeGeneratorPane;

    @FXML
    private Button _wordRepoButton;

    @FXML
    private Tab _savedChallengesTab;

    @FXML
    private Tab _challengeGeneratorTab;
    
    private AnchorPane _wordRepositoryPane = new AnchorPane();
    private Stage _wordRepositoryStage;
    
    private final TabPane _rootNode;
    
    
    
    public ModulesUI(Window parent) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ModulesUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _savedChallengesPane != null;
    	assert _challengeGeneratorPane != null;
    	assert _wordRepoButton != null;
    	
    	initWordRepoButtonHandler(parent);
    }
    
    
    
    private void initWordRepoStage(Window parent)
    {
    	_wordRepositoryStage = new Stage();
    	
    	_wordRepositoryStage.setScene(new Scene(_wordRepositoryPane));
    	_wordRepositoryStage.initOwner(parent);
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
    
    public Tab getSavedChallengesTab()
    {
    	return _savedChallengesTab;
    }
    
    public Tab getChallengeGeneratorTab()
    {
    	return _challengeGeneratorTab;
    }
    
    public ReadOnlyObjectProperty<Tab> selectedTabProperty()
    {
    	return _rootNode.getSelectionModel().selectedItemProperty();
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
