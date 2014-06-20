package regexgolf2.ui.wordrepository;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class WordRepositoryUI
{
    @FXML
    private ListView<?> _listView;

    @FXML
    private Button _removeButton;

    @FXML
    private Button _addButton;

    @FXML
    private Button _saveButton;
    
    private Parent _rootNode;
    
    
    
    public WordRepositoryUI() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("WordRepositoryUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _addButton != null;
    	assert _removeButton != null;
    	assert _saveButton != null;
    	
    	
    }
    
    
    
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
