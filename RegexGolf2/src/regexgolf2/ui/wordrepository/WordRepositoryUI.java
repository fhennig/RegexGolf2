package regexgolf2.ui.wordrepository;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import regexgolf2.ui.wordrepository.wordcell.WordCellUI;
import regexgolf2.ui.wordrepository.wordcell.WordItem;

public class WordRepositoryUI
{
    @FXML
    private ListView<WordItem> _listView;

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
    	
    	_listView.setCellFactory(new Callback<ListView<WordItem>, ListCell<WordItem>>()
		{
			@Override
			public ListCell<WordItem> call(ListView<WordItem> lv)
			{
				ListCell<WordItem> cell = new WordCellUI();
				cell.editableProperty().bind(lv.editableProperty());
				return cell;
			}
		});
    	_listView.setEditable(true);
    }
    
    
    
    public Button getAddButton()
    {
    	return _addButton;
    }
    
    public Button getRemoveButton()
    {
    	return _removeButton;
    }
    
    public Button getSaveButton()
    {
    	return _saveButton;
    }
    
    public ListView<WordItem> getListView()
    {
    	return _listView;
    }
    
    public Parent getUINode()
    {
    	return _rootNode;
    }
}
