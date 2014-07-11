package regexgolf2.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.util.Duration;

import javax.swing.JOptionPane;

import regexgolf2.model.Word;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.ui.wordrepository.WordRepositoryUI;
import regexgolf2.ui.wordrepository.wordcell.WordItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	private final WordRepository _repository;
	private final ChangeListener<Boolean> _outOfSynchListener;



	public WordRepositoryController(WordRepository repository) throws IOException
	{
		_ui = new WordRepositoryUI();
		_repository = repository;
		_outOfSynchListener = createOutOfSynchListener();

		// Disable Remove Button if no Item is selected
		_ui.getRemoveButton().disableProperty()
				.bind(_ui.getListView().getSelectionModel().selectedItemProperty().isNull());

		// Initialize Button Handlers
		_ui.getAddButton().setOnAction(e -> onAddButtonClicked());
		_ui.getRemoveButton().setOnAction(e -> onRemoveButtonClicked());
		_ui.getSaveButton().setOnAction(e -> onSaveButtonClicked());

		// Refresh UI-ItemList if repository changes
		_repository.addServiceChangedListener(e -> refreshListViewItemList());

		// Load Items
		refreshListViewItemList();
	}



	private ChangeListener<Boolean> createOutOfSynchListener()
	{
		return new ChangeListener<Boolean>()
		{
			private int amountOutOfSynch = 0;

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue)
			{
				if (newValue)
					amountOutOfSynch++;
				else
					amountOutOfSynch--;

				_ui.getSaveButton().setDisable(amountOutOfSynch != 0);
			}
		};
	}

	private void onAddButtonClicked()
	{
		Word newWord = _repository.createNew();
		final WordItem item = getItem(newWord);
		_ui.getListView().scrollTo(item);
		/*
		 * Evil hack because edit mode wouldn't start without it if the item
		 * that should be edited is not scrolled into view already. see:
		 * https://stackoverflow
		 * .com/questions/11997041/listview-edit-is-not-switching
		 * -cell-to-input-state
		 */
		new Timeline(new KeyFrame(Duration.seconds(0.01), e -> _ui.getListView().edit(
				_ui.getListView().getItems().indexOf(item)))).play();
	}

	private void onRemoveButtonClicked()
	{
		Word selectedWord = _ui.getListView().getSelectionModel().getSelectedItem().getWord();
		try
		{
			_repository.delete(selectedWord);
		} catch (PersistenceException e)
		{
			// TODO use fancy dialog here
			JOptionPane.showMessageDialog(null, "Error with the Database, could not delete.");
		}
	}

	private void onSaveButtonClicked()
	{
		try
		{
			_repository.saveAll();
		} catch (PersistenceException e)
		{
			// TODO use fancy dialog here
			JOptionPane.showMessageDialog(null, "DB Error");
		}
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
	 * Creates a new Item with the given word and adds it to the UI. The created
	 * Item is returned.
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

	@Requires(
	{ "word != null", "_repository.contains(word)" })
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
