package regexgolf2.ui.subcomponents.challengelisting.challengecell;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import regexgolf2.ui.subcomponents.scoredisplay.SolvedDisplayUI;

public class ChallengeCellUI extends ListCell<ChallengeItem>
{
	@FXML
	private Label _nameLabel;
	
    @FXML
    private Label _scoreLabel;
    private final SolvedDisplayUI _scoreUI;

    @FXML
    private Label _changeIndicator;
	
    private final AnchorPane _rootNode;
    
    private ChallengeItem _item;

    
	
	public ChallengeCellUI() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeCellUI.fxml"));
		loader.setController(this);
		_rootNode = loader.load();
		
		assert _nameLabel != null;
		assert _scoreLabel != null;
		assert _changeIndicator != null;
		
		_scoreUI = new SolvedDisplayUI(_scoreLabel);
		
		this.setPrefWidth(0.0);
		_rootNode.prefWidthProperty().bind(this.widthProperty().subtract(10.0));
		
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
			setGraphic(_rootNode);
			
			_nameLabel.textProperty().unbind();
			_changeIndicator.visibleProperty().unbind();
			_scoreUI.amountRequirementsProperty().unbind();
			_scoreUI.amountCompliedRequirementsProperty().unbind();
			_scoreUI.isHighlightedProperty().unbind();
			
			_item = item;
			
			_nameLabel.textProperty().bind(_item.nameProperty());
			_changeIndicator.visibleProperty().bind(_item.isChangedProperty());
			_scoreUI.amountRequirementsProperty().bind(_item.amountRequirementsProperty());
			_scoreUI.amountCompliedRequirementsProperty().bind(_item.amountCompliedRequirementsProperty());
			_scoreUI.isHighlightedProperty().bind(_item.isSolvedProperty());
		}
	}
	
	@Override
	public void updateSelected(boolean arg0)
	{
		//TODO fix the coloring of the scorelabel text
		super.updateSelected(arg0);
		if (isSelected())
			_scoreLabel.setTextFill(Color.WHITE);
		else
			_scoreUI.refresh();
	}
}
