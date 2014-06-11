package regexgolf2.ui.subcomponents.challengelisting.challengecell;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import regexgolf2.model.Challenge;

public class ChallengeCellUI extends ListCell<ChallengeItem>
{
	@FXML
	private Label _nameLabel;
	
    @FXML
    private Label _scoreLabel;

    @FXML
    private Label _changeIndicator;
	
    private final Node _rootNode;
    
    private ChallengeItem _item;

    
	
	public ChallengeCellUI() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeCellUI.fxml"));
		loader.setController(this);
		_rootNode = loader.load();
		
		assert _nameLabel != null;
		assert _scoreLabel != null;
		assert _changeIndicator != null;
		
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}
	
	
	@Override
	protected void updateItem(ChallengeItem item, boolean empty)
	{
		super.updateItem(item, empty);
		if (item == null)
		{
			setGraphic(null);
		}
		else
		{
			//Because of init with null
			if (_item != null)
			{
				_nameLabel.textProperty().unbindBidirectional(_item.nameProperty());
				_changeIndicator.visibleProperty().unbindBidirectional(_item.isChangedProperty());
			}
			_item = item;
			_nameLabel.textProperty().bindBidirectional(_item.nameProperty());
			_changeIndicator.visibleProperty().bindBidirectional(_item.isChangedProperty());
		}
		
	}
}
