package regexgolf2.ui.subcomponents.requirementlisting;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementCellUI;
import regexgolf2.ui.subcomponents.requirementlisting.requirementcell.RequirementItem;
import regexgolf2.ui.util.DisabledSelectionModel;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class RequirementListUI
{
	private final ListView<RequirementItem> _listView;
	
	
		
	public RequirementListUI()
	{
		_listView = new ListView<RequirementItem>()
				{
					//makes ListView unfocusable
					public void requestFocus() { }
				};
		initListView();
		
		_listView.setSelectionModel(new DisabledSelectionModel<RequirementItem>());
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
		return _listView.isEditable();
	}
	
	public void setEditable(boolean editable)
	{
		_listView.setEditable(editable);
	}
	
	public BooleanProperty editableProperty()
	{
		return _listView.editableProperty();
	}
	
	public Node getUINode()
	{
		return _listView;
	}
}
