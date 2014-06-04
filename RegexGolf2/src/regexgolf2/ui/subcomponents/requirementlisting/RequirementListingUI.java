package regexgolf2.ui.subcomponents.requirementlisting;

import java.util.List;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import com.sun.javafx.collections.ObservableListWrapper;

public class RequirementListingUI
{
	private TableView<RequirementItem> _table;
	private TableColumn<RequirementItem, String> _wordColumn;
	private TableColumn<RequirementItem, String> _isCompliedColumn;
	
	
	
	public RequirementListingUI(TableView<RequirementItem> table)
	{
		_table = table;
		init();
	}
	
	
	
	private void init()
	{
		initWordColumn();
		initIsCompliedColumn();
		_table.getColumns().add(_wordColumn);
		_table.getColumns().add(_isCompliedColumn);
		
	}
	
	private void initWordColumn()
	{
		_wordColumn = new TableColumn<>("Word");
		_wordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RequirementItem,String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<RequirementItem, String> cdf)
			{
				return new ReadOnlyStringWrapper(cdf.getValue().getWord());
			}
		});
	}
	
	private void initIsCompliedColumn()
	{
		_isCompliedColumn = new TableColumn<>("Is Complied");
		_isCompliedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RequirementItem,String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<RequirementItem, String> cdf)
			{
				String displayValue;
				if (cdf.getValue().isComplied())
					displayValue = "Yes";
				else
					displayValue = "No";
				
				return new ReadOnlyStringWrapper(displayValue);
			}
		});
		_isCompliedColumn.setMinWidth(50);
		_isCompliedColumn.setPrefWidth(50);
		_isCompliedColumn.setMaxWidth(50);
		
	}
		
	public void setContent(List<RequirementItem> requirements)
	{
		_table.setItems(new ObservableListWrapper<>(requirements));
	}
	
	public Control getControl()
	{
		return _table;
	}
	
	public void setEditable(boolean editable)
	{
		_table.setEditable(editable);
	}
}
