package regexgolf2.ui.subcomponents.editablelabel;

import javafx.beans.property.BooleanProperty;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * A control that looks like a Label. If clicked, a TextField is shown and the 
 * Text of the Label can be edited.
 * Also displays a Symbol on Mouse-Hover that indicates that editing is possible.
 * Editing can be disabled.
 */
public class EditableLabel
{
	private final AnchorPane _root = new AnchorPane();
	private final TextField _textField = new TextField();
	private final Label _label = new Label();
	private final ImageView _imageView = new ImageView();

	private final StringProperty _text = new SimpleStringProperty();
	private boolean _isEditMode = false;
	private final BooleanProperty _editable = new SimpleBooleanProperty();
	
	
	
	public EditableLabel()
	{		
		_textField.textProperty().bindBidirectional(_text);
		_label.textProperty().bindBidirectional(_text);
		
		initLayout();
		initListeners();
	}
	
	
	
	private void initLayout()
	{
		_textField.prefWidthProperty().bind(_label.widthProperty().add(16));
		_root.prefWidthProperty().bind(_textField.prefWidthProperty().add(_textField.heightProperty()));
		_imageView.setFitHeight(15.0);
		_imageView.setFitWidth(15.0);
				
		AnchorPane.setTopAnchor(_label, 4.0);
		AnchorPane.setLeftAnchor(_label, 8.0);
		
		AnchorPane.setTopAnchor(_textField, 0.0);
		AnchorPane.setLeftAnchor(_textField, 0.0);
				
		AnchorPane.setRightAnchor(_imageView, 2.0);
		AnchorPane.setTopAnchor(_imageView, 5.0);
		AnchorPane.setBottomAnchor(_imageView, 3.0);
		
		_root.getChildren().add(_imageView);
		_root.getChildren().add(_label);
		_root.getChildren().add(_textField);
		
		Image image = new Image(getClass().getResourceAsStream("editSymbol.png"));
		_imageView.setImage(image);
		
		_imageView.setVisible(false);
		_textField.setVisible(false);
	}
	
	/**
	 * If the control switches into edit mode, the textField is made visible and focused.
	 * If edit mode ended, the label is made visible.
	 */
	private void setEditMode(boolean edit)
	{
		if (!isEditable())
			return;
		_textField.setVisible(edit);
		_label.setVisible(!edit);
		if (edit)
		{
			_textField.requestFocus();
			showEditIcon(false);
		}
		_isEditMode = edit;
	}
	
	private void showEditIcon(boolean show)
	{
		if (!isEditable() || _isEditMode)
			return;
		_imageView.setVisible(show);
	}
	
	private void initListeners()
	{
		//TextField enter key pressed
		_textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				setEditMode(false);
			}
		});
		
		//TextField Focus lost
		_textField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2)
			{
				if (!_textField.isFocused())
				{
					setEditMode(false);
				}
			}
		});
		
		//Enable edit mode if clicked.
		_root.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				setEditMode(true);
			}
		});
		
		_root.setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				showEditIcon(true);
			}
		});
		
		_root.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent arg0)
			{
				showEditIcon(false);
			}
		});
	}
	
	@Ensures("result != null")
	public String getText()
	{
		return _text.get();
	}
	
	@Requires("text != null")
	public void setText(String text)
	{
		_text.set(text);
	}
	
	@Ensures("result != null")
	public StringProperty textProperty()
	{
		return _text;
	}
	
	public boolean isEditable()
	{
		return _editable.get();
	}
	
	public void setEditable(boolean editable)
	{
		if (!editable)
			setEditMode(false);
		_editable.set(editable);
	}
	
	@Ensures("result != null")
	public BooleanProperty editableProperty()
	{
		return _editable;
	}
	
	@Ensures("result != null")
	public Node getUINode()
	{
		return _root;
	}
}
