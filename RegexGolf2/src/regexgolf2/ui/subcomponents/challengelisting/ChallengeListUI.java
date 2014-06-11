package regexgolf2.ui.subcomponents.challengelisting;

import java.util.Collection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import regexgolf2.model.SolvableChallenge;

public class ChallengeListUI
{
	private final ListView<SolvableChallenge> _listView = new ListView<>();
	private final BooleanProperty _challengeSelectedProperty = new SimpleBooleanProperty();
	private final ObjectProperty<SolvableChallenge> _selectedChallengeProperty = new SimpleObjectProperty<>();
	
	
	
	public ChallengeListUI()
	{
		initProperties();
	}
	
	
	
	private void initProperties()
	{
		_challengeSelectedProperty.bind(_listView.getSelectionModel().selectedItemProperty().isNull());
		_selectedChallengeProperty.bind(_listView.getSelectionModel().selectedItemProperty());
	}
	
	public void setChallenges(Collection<SolvableChallenge> challenges)
	{
		for (SolvableChallenge sc : _listView.getItems())
		{
			if (!challenges.contains(sc))
				_listView.getItems().remove(sc);
		}
		for (SolvableChallenge sc : challenges)
		{
			if (!_listView.getItems().contains(sc))
				_listView.getItems().add(sc);
		}
	}
	
	public ReadOnlyBooleanProperty challengeSelectedProperty()
	{
		return _challengeSelectedProperty;
	}
	
	public ReadOnlyObjectProperty<SolvableChallenge> selectedChallengeProperty()
	{
		return _selectedChallengeProperty;
	}
	
	public Node getUINode()
	{
		return _listView;
	}
}
