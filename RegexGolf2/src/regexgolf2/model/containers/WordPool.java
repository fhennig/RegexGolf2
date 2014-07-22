package regexgolf2.model.containers;

import java.util.Optional;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

import regexgolf2.model.Word;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.saving.Savable;
import regexgolf2.services.persistence.saving.SaveVisitor;
import regexgolf2.util.Validator;


public class WordPool extends ObservableContainer<Word> implements Savable
{
	private final Validator<String> _textValidator;
	private String _name = "";
	private int _id;
	


	public WordPool()
	{
		_textValidator = createValidator();
	}



	@Ensures("result != null")
	public String getName()
	{
		return _name;
	}
	
	@Requires("name != null")
	public void setName(String name)
	{
		if (_name.equals(name))
			return;
		_name = name;
		fireObjectChangedEvent();
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void setId(int id)
	{
		if (_id == id)
			return;
		_id = id;
		fireObjectChangedEvent();
	}
	
	@Override
	public boolean add(Word item)
	{
		if (getWithText(item.getText()).isPresent())
			return false;
		item.setTextValidator(_textValidator);
		return super.add(item);
	}

	@Ensures(
	{
			"result != null",
			"contains(result)"
	})
	public Word getEmpty()
	{
		Optional<Word> emptyWord = getWithText("");
		if (emptyWord.isPresent())
			return emptyWord.get();
		else
		{
			Word empty = new Word();
			add(empty);
			return empty;
		}
	}

	private Validator<String> createValidator()
	{
		return s -> !getWithText(s).isPresent();
	}

	private Optional<Word> getWithText(String text)
	{
		return stream().filter(w -> w.getText().equals(text)).findFirst();
	}



	@Override
	public void accept(SaveVisitor visitor) throws PersistenceException
	{
		visitor.visit(this);
	}
}
