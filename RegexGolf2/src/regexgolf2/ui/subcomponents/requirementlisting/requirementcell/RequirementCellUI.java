package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import regexgolf2.model.Requirement;
import regexgolf2.ui.subcomponents.editablelabel.EditableLabel;

import com.google.java.contract.Requires;

public class RequirementCellUI extends ListCell<Requirement>
{
	/**
	 * ImageView that displays the check-image that indicates,
	 * if the requirement is complied or not.
	 */
    private final ImageView _imageView = new ImageView();
    
    private final EditableLabel _editLabel = new EditableLabel();

    private final RequirementCellHandler _listener;
    
    private final AnchorPane _rootNode = new AnchorPane();
    
    private Image _notCompliedImage;
    private Image _compliedImage;
    private Requirement _requirement;
    
    
    
    @Requires("listener != null")
    public RequirementCellUI(RequirementCellHandler listener)
    {
    	_listener = listener;
    	initLayout();
    	initImages();
    	initEditLabelListener();
    }
    
    
    
    private void initLayout()
    {
    	Node editLabelNode = _editLabel.getUINode();
    	AnchorPane.setLeftAnchor(editLabelNode, 0.0);
    	AnchorPane.setTopAnchor(editLabelNode, 0.0);
    	AnchorPane.setBottomAnchor(editLabelNode, 0.0);
    	
    	_imageView.setFitHeight(25);
    	_imageView.setFitWidth(25);
    	
    	AnchorPane.setTopAnchor(_imageView, 0.0);
    	AnchorPane.setRightAnchor(_imageView, 0.0);
    	
    	_rootNode.getChildren().add(editLabelNode);
    	_rootNode.getChildren().add(_imageView);
    	
    	//Setup correct scaling
    	this.setPrefWidth(0);
    	_rootNode.prefWidthProperty().bind(this.widthProperty());
    	
    	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }
    
    private void initImages()
    
    {
    	_compliedImage = new Image(getClass().getResourceAsStream("complied.png"));
    	_notCompliedImage = new Image(getClass().getResourceAsStream("notComplied.png"));
    }
    
    private void initEditLabelListener()
    {
    	_editLabel.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2)
			{
				_listener.requirementEdited(RequirementCellUI.this, _editLabel.getText());
			}
		});
    }
    
    /**
     * Sets the text that should be display in the Cell.
     */
    @Requires("word != null")
    public void setWord(String word)
    {
    	_editLabel.setText(word);
    }
    
    public Requirement getRequirement()
    {
    	return _requirement;
    }
    
    public void setComplied(boolean complied)
    {
    	if (complied)
    		_imageView.setImage(_compliedImage);
    	else
    		_imageView.setImage(_notCompliedImage);
    }
    
    public void setIsEditable(boolean editable)
    {
    	_editLabel.setEditable(editable);
    }
    
    /**
     * Called on various events to update the UI accordingly
     */
    @Override
    protected void updateItem(Requirement requirement, boolean empty)
    {
    	super.updateItem(requirement, empty);
    	if (_requirement == requirement)
    		return;
    	_requirement = requirement;
    	if (_requirement == null)
    	{
    		setGraphic(null);
    	}
    	else
    	{
    		setGraphic(_rootNode);
    		_listener.requirementChanged(this, _requirement);
    	}
    }
}