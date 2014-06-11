package regexgolf2.ui.subcomponents.requirementlisting;

import java.util.List;

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
	
	
		
	public RequirementListUI(boolean editable)
	{
		_listView = new ListView<RequirementItem>()
				{
					public void requestFocus() { }
				};
		initListView();
		_listView.setEditable(editable);
		
		_listView.setSelectionModel(new DisabledSelectionModel<RequirementItem>());
	}
	
	
	
	private void initListView()
	{
		_listView.setCellFactory(new Callback<ListView<RequirementItem>, ListCell<RequirementItem>>()
		{
			@Override
			public ListCell<RequirementItem> call(ListView<RequirementItem> lv)
			{
				return new RequirementCellUI();
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
	
	public Node getUINode()
	{
		return _listView;
	}
}
