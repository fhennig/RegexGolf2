package regexgolf2.ui.subcomponents.editablelabel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * A control that looks like a Label. If clicked, a TextField is shown and the 
 * Text of the Label can be edited.
 * Can show a small icon to indicate that editing is possible, this has to be set
 * with the {@link #editIconAppearsProperty()}.
 * Editing can be disabled.
 * EditMode can be entered and exited.
 */
public class EditableLabel
{
	//UI properties
	private final AnchorPane _root = new AnchorPane();
	private final TextField _textField = new TextField();
	private final Label _label = new Label();
	private final ImageView _editIcon = new ImageView();
	private final BooleanProperty _editIconAppears = new SimpleBooleanProperty();

	//Logic properties
	private final StringProperty _text = new SimpleStringProperty();
	private final BooleanProperty _editable = new SimpleBooleanProperty();
	private final BooleanProperty _editMode = new SimpleBooleanProperty();
	
	
	
	public EditableLabel()
	{		
		initBindings();
		initLayout();
		initListeners();
	}
	
	
	
	private void initLayout()
	{
		_textField.prefWidthProperty().bind(_label.widthProperty().add(16));
		_root.prefWidthProperty().bind(_textField.prefWidthProperty().add(_textField.heightProperty()));
		_editIcon.setFitHeight(15.0);
		_editIcon.setFitWidth(15.0);
				
		AnchorPane.setTopAnchor(_label, 4.0);
		AnchorPane.setLeftAnchor(_label, 8.0);
		
		AnchorPane.setTopAnchor(_textField, 0.0);
		AnchorPane.setLeftAnchor(_textField, 0.0);
				
		AnchorPane.setRightAnchor(_editIcon, 2.0);
		AnchorPane.setTopAnchor(_editIcon, 5.0);
		AnchorPane.setBottomAnchor(_editIcon, 3.0);
		
		_root.getChildren().add(_editIcon);
		_root.getChildren().add(_label);
		_root.getChildren().add(_textField);
		
		//XXX here the Image(String) constructor could be used
		Image image = new Image(this.getClass().getResourceAsStream("/regexgolf2/ui/img/edit.png"));
		_editIcon.setImage(image);
	}
	
	private void initBindings()
	{
		//Bind label and textField to textProperty
		_label.textProperty().bind(_text);
		_textField.textProperty().bindBidirectional(_text);
		
		_editIcon.visibleProperty().bind(_editable.and(_editMode.not().and(_editIconAppears)));
		_textField.visibleProperty().bind(_editMode.and(_editable));
		_label.visibleProperty().bind(_editMode.not());
	}
	
	/**
	 * Initializes various Listeners:
	 * - If the Enter-Key is pressed in the TextField, editing mode is exited.
	 * - If the TextField loses focus, editing mode is exited.
	 * - If editing gets disabled, the editing mode is exited.
	 * - If edit mode is entered, the TextField requests focus.
	 */
	private void initListeners()
	{
		//TextField enter key pressed --> exit edit mode
		_textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				exitEditMode();
			}
		});
		
		//TextField Focus lost --> exit edit mode
		_textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue)
			{
				if (!_textField.isFocused())
				{
					exitEditMode();
				}
			}
		});
		
		//editing disabled --> exit edit mode
		_editable.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue)
			{
				if (newValue == false && isEditMode())
					exitEditMode();
			}
		});
		
		//Edit mode true --> focus textfield
		_editMode.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue)
			{
				if (newValue)
					_textField.requestFocus();
			}
		});
	}
	
	//--------------------------- text --------------------------------
	@Ensures("result != null")
	public String getText()
	{
		return _text.get();
	}
	
	@Requires("text != null")
	private void setText(String text)
	{
		_text.set(text);
	}
	
	@Ensures("result != null")
	public StringProperty textProperty()
	{
		return _text;
	}
	
	//--------------------------- editable ----------------------------
	public boolean isEditable()
	{
		return _editable.get();
	}
	
	public void setEditable(boolean editable)
	{
		_editable.set(editable);
	}
	
	@Ensures("result != null")
	public BooleanProperty editableProperty()
	{
		return _editable;
	}
	
	//--------------------------- editMode ----------------------------
	/**
	 * This stops the edit mode.
	 */
	public void exitEditMode()
	{
		_editMode.set(false);
	}
	
	/**
	 * Tries to enter edit mode.
	 * if editing is disabled, this will return false,
	 * else, editing mode will be activated and true is returned. 
	 */
	public boolean tryEnterEditMode()
	{
		if (isEditable())
		{
			_editMode.set(true);
			return true;
		}
		return false;
	}
	
	/**
	 * Indicates if this EditableLabel is currently in editing mode.
	 * @return
	 */
	public boolean isEditMode()
	{
		return _editMode.get();
	}
	
	/**
	 * Readonly Property. If you want to set the editmode, use
	 * {@link #tryEnterEditMode()} or {@link #exitEditMode()}.
	 */
	@Ensures("result != null")
	public ReadOnlyBooleanProperty editModeProperty()
	{
		return _editMode;
	}
	
	//--------------------------- editIconVisible ---------------------
	/**
	 * The edit icon will only be visible if three criteria are met:
	 * 1. Editing is enabled.
	 * 2. Edit mode is currently disabled.
	 * 3. This Property is set to true.
	 * Usually, this is bound to a mouse hover Property to make the icon
	 * show up if the mouse hovers a specific area.
	 */
	@Ensures("result != null")
	public BooleanProperty editIconAppearsProperty()
	{
		return _editIconAppears;
	}
	
	
	
	/**
	 * Returns the Node to be used inside the visual tree.
	 */
	@Ensures("result != null")
	public Node getUINode()
	{
		return _root;
	}
}
