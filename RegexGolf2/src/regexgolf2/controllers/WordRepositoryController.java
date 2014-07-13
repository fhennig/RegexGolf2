package regexgolf2.controllers;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.util.Duration;

import javax.swing.JOptionPane;

import regexgolf2.model.Word;
import regexgolf2.model.containers.ContainerChangedEvent;
import regexgolf2.model.containers.ContainerChangedListener;
import regexgolf2.model.containers.WordPool;
import regexgolf2.model.containers.WordPoolContainer;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.ui.wordrepository.WordPoolItem;
import regexgolf2.ui.wordrepository.WordRepositoryUI;
import regexgolf2.ui.wordrepository.wordcell.WordItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	private final WordPoolContainer _wpc;
	// private final WordPool _pool;
	private final PersistenceService _persistenceService;
	private final ChangeListener<Boolean> _outOfSynchListener;
	private final ContainerChangedListener<Word> _poolChangedListener;
	private final ObjectProperty<WordPool> _selectedPool = new SimpleObjectProperty<>();



	public WordRepositoryController(PersistenceService ps) throws IOException
	{
		_ui = new WordRepositoryUI();
		_persistenceService = ps;
		_wpc = _persistenceService.getWordPoolContainer();
		_outOfSynchListener = createOutOfSynchListener();

		// Disable Remove Button if no Item is selected
		_ui.getRemoveButton().disableProperty()
				.bind(_ui.getListView().getSelectionModel().selectedItemProperty().isNull());

		// Initialize Button Handlers
		_ui.getAddButton().setOnAction(e -> onAddButtonClicked());
		_ui.getRemoveButton().setOnAction(e -> onRemoveButtonClicked());
		_ui.getSaveButton().setOnAction(e -> onSaveButtonClicked());
		_wpc.forEach(pool -> _ui.getWordPoolComboBox().getItems().add(new WordPoolItem(pool)));
		_ui.getWordPoolComboBox().getSelectionModel().selectedItemProperty()
				.addListener((o, oV, nV) -> setSelectedPool(nV.getWordPool()));
		//TODO ensure that there is always a pool selected!

		_poolChangedListener = e -> refreshListViewItemList(e);
		_selectedPool.addListener((o, oV, nV) -> selectedPoolChanged(oV, nV));

		_ui.getWordPoolComboBox().getSelectionModel().selectFirst();
	}

	private void selectedPoolChanged(WordPool oV, WordPool nV)
	{
		if (oV != null)
		{
			oV.removeListener(_poolChangedListener);
		}
		
		nV.addListener(_poolChangedListener);
		_ui.getListView().getItems().clear();
		nV.forEach(word -> addItem(word));
	}

	private ChangeListener<Boolean> createOutOfSynchListener()
	{
		return new ChangeListener<Boolean>()
		{
			private int amountOutOfSynch = 0;

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
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
		Word newWord = getSelectedPool().getEmpty();
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
		getSelectedPool().remove(selectedWord);
	}

	private void onSaveButtonClicked()
	{
		try
		{
			_persistenceService.save(getSelectedPool());
		} catch (PersistenceException e)
		{
			// TODO use fancy dialog here
			JOptionPane.showMessageDialog(null, "DB Error");
		}
	}

	private void refreshListViewItemList(ContainerChangedEvent<? extends Word> e)
	{
		if (e.getAddedItem() != null)
			addItem(e.getAddedItem());
		if (e.getRemovedItem() != null)
			removeItemFor(e.getRemovedItem());
	}

	/**
	 * Creates a new Item with the given word and adds it to the UI. The created
	 * Item is returned.
	 */
	private WordItem addItem(Word word)
	{
		WordItem item = new WordItem(word, _persistenceService.getPersistenceInformation().getPersistenceState(word));
		item.isOutOfSynchPropery().addListener(_outOfSynchListener);
		_ui.getListView().getItems().add(item);
		return item;
	}

	private void removeItemFor(Word word)
	{
		_ui.getListView().getItems().stream()
				.filter(item -> item.getWord().equals(word)).findFirst()
				.ifPresent(item -> {
					_ui.getListView().getItems().remove(item);
					item.isOutOfSynchPropery().removeListener(_outOfSynchListener);
					item.discard();
				});
	}

	@Requires(
	{ "word != null", "getSelectedPool().contains(word)" })
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

	@Ensures("result != null")
	public WordPool getSelectedPool()
	{
		return _selectedPool.get();
	}

	@Requires("pool != null")
	private void setSelectedPool(WordPool pool)
	{
		_selectedPool.set(pool);
	}

	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
