package regexgolf2.ui.wordrepository.wordcell;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import regexgolf2.ui.subcomponents.editablelabel.EditableLabel;

public class WordCellUI extends ListCell<WordItem>
{
	private ImageView _outOfSynchIndicator;
	private EditableLabel _editLabel;
	private Label _changedIndicator;
	private HBox _rootNode;
	
	private WordItem _item;
	
	
	
	public WordCellUI()
	{
		initComponents();
		initLayout();

		this.setPrefWidth(0.0);
		_rootNode.prefWidthProperty().bind(this.widthProperty());
	}
	
	
	
	private void initComponents()
	{
		_outOfSynchIndicator = new ImageView();
		_outOfSynchIndicator.setImage(new Image("/regexgolf2/ui/img/warning.png"));
		_outOfSynchIndicator.setFitHeight(16);
		_outOfSynchIndicator.setPreserveRatio(true);
		
		_editLabel = new EditableLabel();
		_editLabel.editableProperty().bind(this.editableProperty());
		
		_changedIndicator = new Label("*");
		_changedIndicator.setFont(new Font(16.0));
	}
	
	private void initLayout()
	{
		_rootNode = new HBox();
		_rootNode.setSpacing(3);
		_rootNode.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(_editLabel.getUINode(), Priority.ALWAYS);
		_rootNode.getChildren().add(_editLabel.getUINode());
		_rootNode.getChildren().add(_outOfSynchIndicator);
		_rootNode.getChildren().add(_changedIndicator);
	}
	
	private void unbindAll()
	{
		_editLabel.textProperty().unbindBidirectional(_item.textProperty());
		_changedIndicator.visibleProperty().unbind();
		_outOfSynchIndicator.visibleProperty().unbind();
	}
	
	private void bindAll()
	{
		_editLabel.textProperty().bindBidirectional(_item.textProperty());
		_changedIndicator.visibleProperty().bind(_item.isChangedProperty());
		_outOfSynchIndicator.visibleProperty().bind(_item.isOutOfSynchPropery());
	}
	
	//this will be enabled in a later commit
//	@Override
//	public void startEdit()
//	{
//		super.startEdit();
//		_editLabel.requestEditMode(true);
//	}
	
	@Override
	protected void updateItem(WordItem item, boolean empty)
	{
		super.updateItem(item, empty);
		
		if (_item == item)
			return;
		
		if (_item != null)
			unbindAll();
		_item = item;
		if (_item != null)
			bindAll();
		
		if (_item == null)
			setGraphic(null);
		else
			setGraphic(_rootNode);
	}
}
