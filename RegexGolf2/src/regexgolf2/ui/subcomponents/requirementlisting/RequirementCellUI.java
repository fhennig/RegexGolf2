package regexgolf2.ui.subcomponents.requirementlisting;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import regexgolf2.model.Requirement;

import com.google.java.contract.Requires;

public class RequirementCellUI extends ListCell<Requirement>
{
    @FXML
    private ImageView _imageView;

    @FXML
    private TextField _textField;

    @FXML
    private Label _label;

    private final RequirementCellListener _listener;
    
    private final AnchorPane _rootNode;
    
    private Image _notCompliedImage;
    private Image _compliedImage;
    private boolean _editable;
    private Object _item;
    
    
    
    @Requires("listener != null")
    public RequirementCellUI(RequirementCellListener listener) throws IOException
    {
    	_listener = listener;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("RequirementCellUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	_textField.setVisible(false);
    	    	
    	initImages();
    	initListeners();
    }
    
    
    private void initListeners()
    {
    	this.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event arg0)
			{
				reactToLabelClicked();
			}
		});
    	
    	_textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				reactToTextFieldCommit();
			}
		});
    	
    	_textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2)
			{
				if (!_textField.isFocused())
				{
					reactToTextFieldFocusLost();
				}
			}
		});
    }
    
    private void reactToLabelClicked()
    {
    	if (_editable)
    		setEditMode(true);
    }
    
    private void reactToTextFieldCommit()
    {
    	setEditMode(false);
    	_listener.requirementEdited(_textField.getText());
    }
    
    private void reactToTextFieldFocusLost()
    {
    	setEditMode(false);
    }
    
    private void setEditMode(boolean editMode)
    {
    	if (editMode)
    	{
    		_label.setVisible(false);
    		_textField.setText(_label.getText());
    		_textField.setVisible(true);
    		_textField.requestFocus();
    	}
    	else
    	{
    		_textField.setVisible(false);
    		_label.setVisible(true);
    		_label.requestFocus();
    	}
    }
    
    private void initImages()
    {
    	_compliedImage = new Image(getClass().getResourceAsStream("complied.png"));
    	_notCompliedImage = new Image(getClass().getResourceAsStream("notComplied.png"));
    }
    
    public void setWord(String word)
    {
    	_label.setText(word);
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
    	_editable = editable;
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