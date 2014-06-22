package regexgolf2.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

import javax.swing.JOptionPane;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.model.Word;
import regexgolf2.services.ServiceChangedListener;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.ui.wordrepository.WordRepositoryUI;
import regexgolf2.ui.wordrepository.wordcell.WordItem;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	private final WordRepository _repository;
	private ChangeListener<Boolean> _outOfSynchListener;
	
	
	
	public WordRepositoryController(WordRepository repository) throws IOException
	{
		_ui = new WordRepositoryUI();
		_repository = repository;
		initBindings();
		initButtonHandlers();
		initRepositoryListener();
		initOutOfSynchListener();
		
		refreshListViewItemList();
	}
	
	
	
	private void initOutOfSynchListener()
	{
		_outOfSynchListener = new ChangeListener<Boolean>()
		{
			private int amountOutOfSynch = 0;
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue)
			{
				if (newValue)
					amountOutOfSynch++;
				else
					amountOutOfSynch--;
				
				_ui.getSaveButton().setDisable(amountOutOfSynch != 0);
					
				
//				if (newValue)
//					_ui.getSaveButton().setDisable(true);
//				else
//				{
//					for (WordItem item : _ui.getListView().getItems())
//					{
//						if (item.isOutOfSynch())
//						{
//							_ui.getSaveButton().setDisable(true);
//							return;
//						}
//					}
//					_ui.getSaveButton().setDisable(false);
//				}
			}
		};
	}
	
	private void initButtonHandlers()
	{
		_ui.getAddButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				Word newWord = _repository.createNew();
				WordItem item = getItem(newWord);
				_ui.getListView().edit(_ui.getListView().getItems().indexOf(item));
			}
		});
		
		_ui.getRemoveButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				Word selectedWord = _ui.getListView().getSelectionModel().getSelectedItem().getWord();
				try
				{
					_repository.delete(selectedWord);
				} catch (SQLException e)
				{
					//TODO use fancy dialog here
					JOptionPane.showMessageDialog(null, "Error with the Database, could not delete.");
				}
			}
		});
		
		_ui.getSaveButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					_repository.saveAll();
				} catch (SQLException e)
				{
					//TODO use fancy dialog here
					JOptionPane.showMessageDialog(null, "DB Error");
				}
			}
		});
	}
	
	private void initBindings()
	{
		_ui.getRemoveButton().disableProperty().bind(
				_ui.getListView().getSelectionModel().selectedItemProperty().isNull());
	}
	
	/**
	 * Initializes a Listener on the Repository,
	 * that updates the UI List if the Repository changes.
	 */
	private void initRepositoryListener()
	{
		_repository.addServiceChangedListener(new ServiceChangedListener()
		{
			@Override
			public void serviceChanged(EventObject event)
			{
				refreshListViewItemList();
			}
		});
	}
	
	private void refreshListViewItemList()
	{
		List<WordItem> toRemove = new ArrayList<>();
		for (WordItem item : _ui.getListView().getItems())
		{
			if (!_repository.contains(item.getWord()))
				toRemove.add(item);
		}
		for (WordItem item : toRemove)
			removeItem(item);
		
		for (Word w : _repository.getAll())
		{
			boolean itemExists = false;
			for (WordItem item : _ui.getListView().getItems())
			{
				if (item.getWord() == w)
				{
					itemExists = true;
					break;
				}
			}
			if (!itemExists)
				addItem(w);
		}
	}
	
	/**
	 * Creates a new Item with the given word and adds it to the UI.
	 * The created Item is returned.
	 */
	private WordItem addItem(Word word)
	{
		WordItem item = new WordItem(word, _repository.getPersistenceState(word));
		item.isOutOfSynchPropery().addListener(_outOfSynchListener);
		_ui.getListView().getItems().add(item);
		return item;
	}
	
	private void removeItem(WordItem item)
	{
		_ui.getListView().getItems().remove(item);
		item.isOutOfSynchPropery().removeListener(_outOfSynchListener);
		item.discard();
	}
	
	@Requires({
		"word != null",
		"_repository.contains(word)"
	})
	@Ensures("result != null")
	private WordItem getItem(Word word)
	{
		for (WordItem item : _ui.getListView().getItems())
		{
			if (item.getWord().equals(word))
			{
				_ui.getListView().getSelectionModel().select(item);
				return item;
			}
		}
		throw new IllegalArgumentException("An item for '" + word + "' could not be found.");
	}
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
