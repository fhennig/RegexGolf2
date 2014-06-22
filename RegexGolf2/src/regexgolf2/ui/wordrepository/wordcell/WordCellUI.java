package regexgolf2.ui.wordrepository.wordcell;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import regexgolf2.ui.util.EditableListCell;

/**
 * The WordCellUI consists of the inherited editable Label and
 * two indicators.
 * One indicates that the entered Word cannot be safed, because it is a duplicate,
 * the other indicates that the word changed.
 */
public class WordCellUI extends EditableListCell<WordItem>
{
	private ImageView _outOfSynchIndicator;
	private Label _changedIndicator;
	private HBox _rootNode;
	
	
	
	public WordCellUI()
	{
		initComponents();
		initLayout();
		setContent(_rootNode);
	}
	
	
	
	private void initComponents()
	{
		_outOfSynchIndicator = new ImageView();
		_outOfSynchIndicator.setImage(new Image("/regexgolf2/ui/img/warning.png"));
		_outOfSynchIndicator.setFitHeight(16);
		_outOfSynchIndicator.setPreserveRatio(true);
		
		_changedIndicator = new Label("*");
		_changedIndicator.setFont(new Font(16.0));
	}
	
	private void initLayout()
	{
		_rootNode = new HBox();
		_rootNode.setSpacing(3);
		_rootNode.setAlignment(Pos.CENTER_LEFT);
		_rootNode.getChildren().add(_outOfSynchIndicator);
		_rootNode.getChildren().add(_changedIndicator);
	}
	
	@Override
	protected void unbind()
	{
		this.textProperty().unbindBidirectional(getItem().textProperty());
		_changedIndicator.visibleProperty().unbind();
		_outOfSynchIndicator.visibleProperty().unbind();
	}
	
	@Override
	protected void bind()
	{
		this.textProperty().bindBidirectional(getItem().textProperty());
		_changedIndicator.visibleProperty().bind(getItem().isChangedProperty());
		_outOfSynchIndicator.visibleProperty().bind(getItem().isOutOfSynchPropery());
	}
}
