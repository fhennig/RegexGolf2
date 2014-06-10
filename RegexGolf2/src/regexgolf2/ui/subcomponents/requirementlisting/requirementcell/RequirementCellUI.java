package regexgolf2.ui.subcomponents.requirementlisting.requirementcell;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
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
    @FXML
    private ImageView _imageView;
    
    private EditableLabel _editLabel = new EditableLabel();

    private final RequirementCellListener _listener;
    
    private final AnchorPane _rootNode;
    
    private Image _notCompliedImage;
    private Image _compliedImage;
    private Object _item;
    
    
    
    @Requires("listener != null")
    public RequirementCellUI(RequirementCellListener listener) throws IOException
    {
    	_listener = listener;
    	
    	_rootNode = new AnchorPane();
    	
    	Node editLabel = _editLabel.getUINode();
    	
    	AnchorPane.setLeftAnchor(editLabel, 0.0);
    	AnchorPane.setTopAnchor(editLabel, 1.0);
    	AnchorPane.setBottomAnchor(editLabel, 1.0);
    	
    	_imageView = new ImageView();
    	_imageView.setFitHeight(25);
    	_imageView.setFitWidth(25);
    	
    	AnchorPane.setTopAnchor(_imageView, 0.0);
    	AnchorPane.setRightAnchor(_imageView, 0.0);
    	
    	_rootNode.getChildren().add(editLabel);
    	_rootNode.getChildren().add(_imageView);
    	
    	//Setup correct scaling
    	this.setPrefWidth(0);
    	_rootNode.prefWidthProperty().bind(this.widthProperty().subtract(14));
    	
    	initImages();
    	initListeners();
    }
    
    
    private void initListeners()
    {
    	_editLabel.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2)
			{
				_listener.requirementEdited(_editLabel.getText());
			}
		});
    }

    private void initImages()
    {
    	_compliedImage = new Image(getClass().getResourceAsStream("complied.png"));
    	_notCompliedImage = new Image(getClass().getResourceAsStream("notComplied.png"));
    }
    
    public void setWord(String word)
    {
    	_editLabel.setText(word);
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
    	if (_item == requirement)
    		return;
    	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    	if (requirement == null)
    	{
    		setGraphic(null);
    	}
    	else
    	{
    		setGraphic(_rootNode);
    		_listener.requirementChanged(requirement);
    	}
    	_item = requirement;
    }
}