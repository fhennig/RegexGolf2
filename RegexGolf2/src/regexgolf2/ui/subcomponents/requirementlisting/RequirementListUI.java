package regexgolf2.ui.subcomponents.requirementlisting;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import regexgolf2.model.Requirement;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class RequirementListUI
{
	private final ListView<Requirement> _listView = new ListView<>();
	private final RequirementCellFactory _factory;
	
	
		
	@Requires("factory != null")
	public RequirementListUI(RequirementCellFactory factory)
	{
		_factory = factory;
		initListView();
	}
	
	
	
	private void initListView()
	{
		_listView.setCellFactory(new Callback<ListView<Requirement>, ListCell<Requirement>>()
		{
			@Override
			public ListCell<Requirement> call(ListView<Requirement> lv)
			{
				ListCell<Requirement> cell = _factory.createCell();
				return cell;
			}
		});
	}
	
	@Requires("requirement != null")
	public void addRequirement(Requirement requirement)
	{
		_listView.getItems().add(requirement);
	}
	
	@Requires("requirement != null")
	public void removeRequirement(Requirement requirement)
	{
		_listView.getItems().remove(requirement);
	}
	
	@Ensures("result != null")
	public List<Requirement> getItems()
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
