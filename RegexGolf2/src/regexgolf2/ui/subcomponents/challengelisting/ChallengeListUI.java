package regexgolf2.ui.subcomponents.challengelisting;

import java.io.IOException;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeCellUI;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeItem;

public class ChallengeListUI
{
	private final ListView<ChallengeItem> _listView = new ListView<>();
	private final BooleanProperty _challengeSelectedProperty = new SimpleBooleanProperty();
	private final ObjectProperty<ChallengeItem> _selectedChallengeProperty = new SimpleObjectProperty<>();
	
	
	
	public ChallengeListUI()
	{
		initProperties();
		initListView();
	}
	
	
	
	private void initProperties()
	{
		_challengeSelectedProperty.bind(_listView.getSelectionModel().selectedItemProperty().isNull());
		_selectedChallengeProperty.bind(_listView.getSelectionModel().selectedItemProperty());
	}
	
	private void initListView()
	{
		_listView.setCellFactory(new Callback<ListView<ChallengeItem>, ListCell<ChallengeItem>>()
		{
			@Override
			public ListCell<ChallengeItem> call(ListView<ChallengeItem> lv)
			{
				try
				{
					return new ChallengeCellUI();
				} catch (IOException e)
				{
					return new ListCell<>();
					//TODO handle this exception with a Dialog
				}
			}
		});
	}
	
	public void select(ChallengeItem item)
	{
		_listView.getSelectionModel().select(item);
	}
	
	public List<ChallengeItem> getChallengeItems()
	{
		return _listView.getItems();
	}
	
	public ReadOnlyBooleanProperty challengeSelectedProperty()
	{
		return _challengeSelectedProperty;
	}
	
	public ReadOnlyObjectProperty<ChallengeItem> selectedChallengeProperty()
	{
		return _selectedChallengeProperty;
	}
	
	public Node getUINode()
	{
		return _listView;
	}
}
