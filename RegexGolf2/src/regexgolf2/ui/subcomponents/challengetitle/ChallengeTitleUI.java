package regexgolf2.ui.subcomponents.challengetitle;

import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.dialog.Dialogs;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeTitleUI
{
	private final HBox _root = new HBox();
	private final Label _nameLabel = new Label();
	private final ImageView _imageView = new ImageView();
	private final PopOver _popOver = new PopOver();
	private final TextField _textField = new TextField();
	
	private final BooleanProperty _editable = new SimpleBooleanProperty();
	private final ObjectProperty<Font> _font = new SimpleObjectProperty<>();
	private final StringProperty _text = new SimpleStringProperty();
	
	
	
	public ChallengeTitleUI()
	{
		setFont(new Font(20));
		setEditable(true);
		initLayout();
		initHandlers();
		initPopOver();
	}
	
	
	
	private void initLayout()
	{
		
		_nameLabel.fontProperty().bind(_font);
		_textField.fontProperty().bind(_font);
		
		_nameLabel.textProperty().bind(_text);
		_textField.textProperty().bindBidirectional(_text);
		
		_root.spacingProperty().bind(_nameLabel.heightProperty().multiply(0.6));
		_root.setAlignment(Pos.CENTER_LEFT);
		
		_imageView.visibleProperty().bind(_root.hoverProperty().and(_editable));
//		_imageView.fitHeightProperty().bind(_nameLabel.heightProperty().multiply(0.75));
		_imageView.setFitHeight(18); //TODO fix Bug with scaling issues on initialization
		_imageView.setPreserveRatio(true);
		Image editImage = new Image(this.getClass().getResourceAsStream("/regexgolf2/ui/img/edit.png"));
		_imageView.setImage(editImage);
		
		_root.getChildren().addAll(_nameLabel, _imageView);
	}
	
	private void initHandlers()
	{		
		_root.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if (isEditable())
				{
					//TODO fix PopOver-Exception
//					_popOver.show(_nameLabel);
					
					//Workaround while PopOver is unusable: Use a Dialog
					Optional<String> result = Dialogs.create()
								.title("Enter Challenge Name")
								.message("Challenge Name: ")
								//.style(DialogStyle.UNDECORATED)
								.showTextInput(getText());
					if (result.isPresent())
						setText(result.get());
						
				}
			}
		});
		
		_textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				_popOver.hide();
			}
		});
	}
	
	private void initPopOver()
	{
		HBox box = new HBox();
		box.getChildren().add(_textField);
		HBox.setMargin(_textField, new Insets(13.0));
		
		_popOver.setContentNode(box);
		_popOver.setDetachable(false);
		_popOver.setAutoHide(true);
		_popOver.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
	}
	
	public String getText()
	{
		return _text.get();
	}
	
	public void setText(String text)
	{
		_text.set(text);
	}
	
	public StringProperty textProperty()
	{
		return _text;
	}
	
	@Ensures("result != null")
	public Font getFont()
	{
		return _font.get();
	}
	
	@Requires("font != null")
	public void setFont(Font font)
	{
		_font.set(font);
	}
	
	public ObjectProperty<Font> fontProperty()
	{
		return _font;
	}
	
	public boolean isEditable()
	{
		return _editable.get();
	}
	
	public void setEditable(boolean editable)
	{
		_editable.set(editable);
	}
	
	public BooleanProperty editableProperty()
	{
		return _editable;
	}
	
	public Node getUINode()
	{
		return _root;
	}
}
