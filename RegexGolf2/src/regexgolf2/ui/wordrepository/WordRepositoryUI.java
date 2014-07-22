package regexgolf2.ui.wordrepository;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import regexgolf2.ui.subcomponents.wordpoolcombobox.WordPoolComboBox;
import regexgolf2.ui.util.JavafxUtil;
import regexgolf2.ui.wordrepository.wordcell.WordCellUI;
import regexgolf2.ui.wordrepository.wordcell.WordItem;

public class WordRepositoryUI
{
    @FXML
    private AnchorPane _poolComboBoxPane;
    private WordPoolComboBox _poolComboBox;
    
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
    	
    	assert _poolComboBoxPane != null;
    	assert _listView != null;
    	assert _removeButton != null;
    	assert _addButton != null;
    	assert _saveButton != null;
    	assert _rootNode != null;
    	
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
    	
    	_poolComboBox = new WordPoolComboBox();
    	JavafxUtil.setAsContent(_poolComboBox.getUINode(), _poolComboBoxPane);
    }
    
    
    
    public WordPoolComboBox getWordPoolComboBox()
    {
    	return _poolComboBox;
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
