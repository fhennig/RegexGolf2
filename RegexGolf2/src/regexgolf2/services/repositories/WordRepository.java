package regexgolf2.services.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import regexgolf2.model.Word;
import regexgolf2.services.ObservableService;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.changetracking.PersistenceState;
import regexgolf2.services.persistence.changetracking.PersistenceStateImpl;
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
	public WordRepository(WordMapper mapper) throws PersistenceException
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
	
	private void reloadAll() throws PersistenceException
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
	
	/**
	 * This method searches for a word with the given text.
	 * If <code> text </code> is null, the result will be null too.
	 * If no word with the given text could be found, null is returned.
	 */
	public Word getWord(String text)
	{
		for (Word w : getAll())
		{
			if (w.getText().equals(text))
				return w;
		}
		return null;
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
	
	/**
	 * This method will attempt to create a new empty word.
	 * Because every word needs to have a unique text,
	 * if an empty word already exists, no new word is created,
	 * the empty word is returned.
	 * @return
	 */
	@Ensures("result != null")
	public Word createNew()
	{
		Word emptyWord = getWord("");
		if (emptyWord != null)
			return emptyWord;
		Word w = new Word();
		w.setTextValidator(_wordValidator);
		_persistenceStates.put(w, new PersistenceStateImpl(w, true));
		fireServiceChangedEvent();
		return w;
	}
	
	public void saveAll() throws PersistenceException
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
	public void save(Word word) throws PersistenceException
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
	public void delete(Word word) throws PersistenceException
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
