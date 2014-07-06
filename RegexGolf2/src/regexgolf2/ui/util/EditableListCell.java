package regexgolf2.ui.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import regexgolf2.ui.subcomponents.editablelabel.EditableLabel;

/**
 * The EditableListCell consists of two main UI components. On the left is the
 * editable Label and on the right is a Panel that can be filled by Subclasses
 * via the {@link #setContent(Node)} method. To access the text of the editable
 * Label, simply use the {@link #textProperty()}.
 */
public class EditableListCell<T> extends ListCell<T>
{
	private HBox _rootNode;
	private EditableLabel _editLabel;
	private AnchorPane _extraContentPanel;



	public EditableListCell()
	{
		initComponents();

		this.setPrefWidth(0.0);
		_rootNode.prefWidthProperty().bind(this.widthProperty().subtract(10));
		this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}



	private void initComponents()
	{
		_editLabel = new EditableLabel();
		_editLabel.editableProperty().bind(this.editableProperty());
		_editLabel.editIconAppearsProperty().bind(this.hoverProperty());
		_editLabel.textProperty().bindBidirectional(this.textProperty());
		// If editLabel exits editmode, commit edit.
		_editLabel.editModeProperty().addListener((o, oV, editMode) ->
		{
			if (!editMode)
				EditableListCell.this.commitEdit(EditableListCell.this.getItem());
		});

		_extraContentPanel = new AnchorPane();

		HBox.setHgrow(_editLabel.getUINode(), Priority.ALWAYS);
		_rootNode = new HBox(_editLabel.getUINode(), _extraContentPanel);
		_rootNode.setAlignment(Pos.CENTER);
	}

	/**
	 * Sets the content for the Panel to the right of the editable Label.
	 * 
	 * @param node
	 *            The content node, or null to clear the Panel.
	 */
	protected void setContent(Node node)
	{
		JavafxUtil.setAsContent(node, _extraContentPanel);
	}

	/**
	 * This method is called if the item changes. Inside this method, every
	 * Property that was bound to the Item somehow; should unbind again.
	 * {@link #getItem()} will not return null, it is save to call.
	 */
	protected void unbind()
	{

	}

	/**
	 * This method is called after the item was updated. If you need to make
	 * bindings to the item, do them here.
	 */
	protected void bind()
	{

	}

	@Override
	public final void startEdit()
	{
		super.startEdit();
		_editLabel.tryEnterEditMode();
	}

	@Override
	protected final void updateItem(T item, boolean empty)
	{
		// If the new item is equal to the current, we only call super
		if (item == getItem())
		{
			super.updateItem(item, empty);
			return;
		}

		if (getItem() != null)
			unbind();

		super.updateItem(item, empty);

		if (getItem() != null)
			bind();

		if (getItem() != null)
			setGraphic(_rootNode);
		else
			setGraphic(null);
	}
}
