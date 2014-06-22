package regexgolf2.ui.wordrepository.wordcell;

import java.util.EventObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.Word;
import regexgolf2.services.repositories.PersistenceState;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class WordItem
{
	private final StringProperty _text = new SimpleStringProperty();
	private final BooleanProperty _isChanged = new SimpleBooleanProperty();
	private final BooleanProperty _isOutOfSynch = new SimpleBooleanProperty();
	private final Word _word;
	private final PersistenceState _ps;
	private ObjectChangedListener _listener;
	
	
	
	@Requires({
		"word != null",
		"ps != null",
		"ps.getObservedItem() == word"
	})
	public WordItem(Word word, PersistenceState ps)
	{
		_word = word;
		_ps = ps;
		_text.set(_word.getText());
		_isChanged.set(ps.isChanged());
		
		initListeners();
	}
	
	
	
	private void initListeners()
	{
		_text.addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> o,
					String oV, String nV)
			{
				//FIXME this does not work, fix
				boolean success = _word.trySetText(nV);
				_isOutOfSynch.set(!success);
			}
		});
		
		_listener = new ObjectChangedListener()
		{
			@Override
			public void objectChanged(EventObject event)
			{
				_text.set(_word.getText());
				_isChanged.set(_ps.isChanged());
			}
		};
		
		_word.addObjectChangedListener(_listener);
		_ps.addObjectChangedListener(_listener);
	}
	
	@Ensures("result != null")
	public Word getWord()
	{
		return _word;
	}
	
	@Ensures("result != null")
	public StringProperty textProperty()
	{
		return _text;
	}
	
	@Ensures("result != null")
	public ReadOnlyBooleanProperty isChangedProperty()
	{
		return _isChanged;
	}
	
	@Ensures("result != null")
	public ReadOnlyBooleanProperty isOutOfSynchPropery()
	{
		return _isOutOfSynch;
	}
	
	/**
	 * This should be called to avoid memory leaks.
	 * This will remove the listener from the Word and PersistenceState,
	 * allowing the garbage collector to collect this WordItem.
	 */
	public void discard()
	{
		_word.removeObjectChangedListener(_listener);
		_ps.removeObjectChangedListener(_listener);
	}
}
