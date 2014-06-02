package regexgolf2.ui.requirementlisting;

import java.util.EventObject;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import regexgolf2.model.requirement.MatchResultChangedListener;
import regexgolf2.model.requirement.Requirement;
import regexgolf2.model.requirement.WordChangedListener;

import com.sun.javafx.collections.ObservableListWrapper;

public class RequirementListingUIOld
{
	private TableView<Requirement> _table;
	private TableColumn<Requirement, String> _wordColumn;
	private TableColumn<Requirement, String> _isCompliedColumn;
	
	
	
	public RequirementListingUIOld(TableView<Requirement> table)
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
		_wordColumn.setCellFactory(TextFieldTableCell.<Requirement>forTableColumn());
		_wordColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Requirement,String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<Requirement, String> cdf)
			{
				return new WordColumnProperty(cdf.getValue());
			}
		});
		_wordColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Requirement,String>>()
		{
			@Override
			public void handle(CellEditEvent<Requirement, String> event)
			{
				event.getRowValue().setWord(event.getNewValue());
			}
		});
		_wordColumn.setEditable(true);
	}
	
	private void initIsCompliedColumn()
	{
		_isCompliedColumn = new TableColumn<>("Is Complied");
		_isCompliedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Requirement,String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<Requirement, String> cdf)
			{
				return new IsCompliedColumnProperty(cdf.getValue());
			}
		});
	}
	
	
	private static class WordColumnProperty extends SimpleStringProperty
	implements WordChangedListener, EventHandler<CellEditEvent<Requirement, String>>
	{
		private final Requirement _requirement;
		
		
		public WordColumnProperty(Requirement requirement)
		{
			_requirement = requirement;
			_requirement.addWordChangedListener(this);
			refresh();
		}
		
		private void refresh()
		{
			this.setValue(_requirement.getWord());
		}

		@Override
		public void handle(CellEditEvent<Requirement, String> event)
		{
			_requirement.setWord(event.getNewValue());
		}

		@Override
		public void wordChanged(EventObject event) { refresh(); }
		
	}
	
	private static class IsCompliedColumnProperty extends SimpleStringProperty implements MatchResultChangedListener
	{
//		private final Requirement _requirement;
		
		
		public IsCompliedColumnProperty(Requirement requirement)
		{
//			_requirement = requirement;
//			_requirement.addMatchResultChangedListener(this);
//			refresh();
		}
		
		private void refresh()
		{
//			String s = new Boolean(_requirement.isComplied()).toString();
//			this.setValue(s);
		}

		@Override
		public void matchResultChanged(EventObject event)
		{
			refresh();
		}
	}
	
	
	public void setContent(List<Requirement> requirements)
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
