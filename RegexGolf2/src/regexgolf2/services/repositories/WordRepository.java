package regexgolf2.services.repositories;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexgolf2.model.Word;
import regexgolf2.services.ObservableService;
import regexgolf2.services.persistence.mappers.WordMapper;
import regexgolf2.util.Validator;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

/**
 * The WordRepository contains all the words from the database.
 * It also ensures that every word has a unique text.
 */
public class WordRepository extends ObservableService
{
	private final WordMapper _mapper;
	/**
	 * This map saves the persistenceStates for the Words.
	 * Also, the keySet is used as a container for the words themselfes.
	 * This makes it easier to handle, because there is just one collection that needs to be updated etc.
	 */
	private final Map<Word, PersistenceStateImpl> _persistenceStates = new HashMap<>();
	private Validator<String> _wordValidator;
	
	
	
	@Requires("mapper != null")
	public WordRepository(WordMapper mapper) throws SQLException
	{
		_mapper = mapper;
		initWordValidator();
		reloadAll();
	}
	
	
	
	/**
	 * Initializes a Validator that ensures that every Word
	 * has a unique text. This is also specified in the database.
	 */
	private void initWordValidator()
	{
		_wordValidator = new Validator<String>()
		{
			@Override
			public boolean isValid(String text)
			{
				for (Word w : getAll())
				{
					if (w.getText().equals(text))
						return false;
				}
				return true;
			}
		};
	}
	
	private void reloadAll() throws SQLException
	{
		_persistenceStates.clear();
		
		List<Word> words = _mapper.getAll();
		
		for (Word word : words)
		{
			word.setTextValidator(_wordValidator);
			PersistenceStateImpl ps = new PersistenceStateImpl(word, false);
			_persistenceStates.put(word, ps);
		}
		fireServiceChangedEvent();
	}

	/**
	 * Returns an <b>unmodifiable</b> Set.
	 */
	@Ensures("result != null")
	public Set<Word> getAll()
	{
		return Collections.unmodifiableSet(_persistenceStates.keySet());
	}
	
	@Requires({
		"word != null",
		"contains(word)"
	})
	@Ensures("result != null")
	public PersistenceState getPersistenceState(Word word)
	{
		return _persistenceStates.get(word);
	}
	
	@Ensures("result != null")
	public Word createNew()
	{
		Word w = new Word();
		w.setTextValidator(_wordValidator);
		_persistenceStates.put(w, new PersistenceStateImpl(w, true));
		fireServiceChangedEvent();
		return w;
	}
	
	public void saveAll() throws SQLException
	{
		for (Word word : getAll())
		{
			if (getPersistenceState(word).isChanged())
				save(word);
		}
	}
	
	@Requires({
		"word != null",
		"contains(word)"
	})
	@Ensures("getPersistenceState(word).isNew() == false")
	public void save(Word word) throws SQLException
	{
		boolean isNew = getPersistenceState(word).isNew();
		
		if (isNew)
			_mapper.insert(word);
		else
			_mapper.update(word);
		
		resetPersistenceState(word);
	}
	
	@Requires({
		"word != null",
		"contains(word)"
	})
	@Ensures("!contains(word)")
	public void delete(Word word) throws SQLException
	{
		if (!getPersistenceState(word).isNew())
			_mapper.delete(word.getId());
		_persistenceStates.remove(word);
		word.setTextValidator(null);
		fireServiceChangedEvent();
	}
	
	/**
	 * Indicates if this repository contains the given word.
	 */
	public boolean contains(Word word)
	{
		if (word == null)
			return false;
		return _persistenceStates.keySet().contains(word);
	}
	
	@Requires({
		"word != null",
		"contains(word)"
	})
	private void resetPersistenceState(Word word)
	{
		_persistenceStates.get(word).objectWasPersisted();
	}
}
