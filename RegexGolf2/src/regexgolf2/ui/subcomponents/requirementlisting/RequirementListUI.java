package regexgolf2.ui.subcomponents.requirementlisting;

import java.io.IOException;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellUI;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class RequirementListUI
{
	private BooleanProperty _editableProperty = new SimpleBooleanProperty();
	
	@FXML
	private Label _titleLabel;
	
	@FXML
	private Button _addButton;

    @FXML
    private Button _removeButton;
	
    @FXML
    private ListView<RequirementItem> _listView;
    
    private final Node _rootNode;
    
    
		
	public RequirementListUI() throws IOException
	{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RequirementListUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
		
    	assert _titleLabel != null;
    	assert _addButton != null;
    	assert _removeButton != null;
    	assert _listView != null;
    	
		initListView();
		initBindings();
	}
	
	
	private void initBindings()
	{
		_addButton.visibleProperty().bind(_editableProperty);
		_removeButton.visibleProperty().bind(_editableProperty);
		_removeButton.disableProperty().bind(_listView.getSelectionModel().selectedItemProperty().isNull());
		_listView.editableProperty().bind(_editableProperty);
		
		_listView.mouseTransparentProperty().bind(_editableProperty.not());
		_listView.focusTraversableProperty().bind(_editableProperty);
		
		_editableProperty.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue)
			{
				if (!newValue)
					_listView.getSelectionModel().clearSelection();
			}
		});
	}
	
	
	private void initListView()
	{
		_listView.setCellFactory(new Callback<ListView<RequirementItem>, ListCell<RequirementItem>>()
		{
			@Override
			public ListCell<RequirementItem> call(ListView<RequirementItem> lv)
			{
				RequirementCellUI cellUI = new RequirementCellUI();
				cellUI.editableProperty().bind(lv.editableProperty());
				return cellUI;
			}
		});
	}

	
	public Label getTitleLabel()
	{
		return _titleLabel;
	}
	
	public Button getAddButton()
	{
		return _addButton;
	}
	
	public Button getRemoveButton()
	{
		return _removeButton;
	}
	
	@Requires("requirementItem != null")
	public void addRequirementItem(RequirementItem requirementItem)
	{
		_listView.getItems().add(requirementItem);
	}
	
	@Requires("requirementItem != null")
	public void removeRequirementItem(RequirementItem requirementItem)
	{
		_listView.getItems().remove(requirementItem);
	}
	
	@Ensures("result != null")
	public List<RequirementItem> getItems()
	{
		return _listView.getItems();
	}
	
	public void clear()
	{
		_listView.getItems().clear();
	}
	
	public boolean isEditable()
	{
		return _editableProperty.get();
	}
	
	public void setEditable(boolean editable)
	{
		_editableProperty.set(editable);
	}
	
	public BooleanProperty editableProperty()
	{
		return _editableProperty;
	}
	
	public RequirementItem getSelectedItem()
	{
		return _listView.getSelectionModel().getSelectedItem();
	}
	
	public Node getUINode()
	{
		return _rootNode;
	}
}
