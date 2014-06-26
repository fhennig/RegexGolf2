package regexgolf2.ui.main;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import regexgolf2.ui.util.JavafxUtil;

import com.google.java.contract.Ensures;

public class MainUI
{
    @FXML
    private AnchorPane _mainPane;

    @FXML
    private AnchorPane _modulesPane;
    
    private final Parent _rootNode;

    
        
    public MainUI(final Stage stage) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _mainPane != null;
    	assert _modulesPane != null;
    	
    	Scene mainScene = new Scene(_rootNode);
    	stage.setScene(mainScene);
    	stage.setTitle("RegexGolf");
    	stage.setWidth(800);
    	stage.setHeight(500);

    	stage.show();
    }
     
    
    
    public void setMainPaneContent(Node content)
    {
    	JavafxUtil.setAsContent(content, _mainPane);
    }

    public void setModulesPaneContent(Node content)
    {
    	JavafxUtil.setAsContent(content, _modulesPane);
    }
    
    //TODO this method is not needed anymore
    @Ensures("result != null")
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
