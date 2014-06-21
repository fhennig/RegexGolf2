package regexgolf2.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import regexgolf2.util.Validator;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Word extends ObservableObject
{
	private static final Logger _LOG = Logger.getLogger(Word.class.getName());
	
	private String _text = "";
	private int _id;
	/**
	 * Initialize the textValidator with null.
	 * That means, input will not be validated.
	 */
	private Validator<String> _textValidator = null;
		
	
	
	public Word()
	{
		_LOG.setLevel(Level.ALL);
	}
	
	
	
	@Ensures("result != null")
	public String getText()
	{
		return _text;
	}
	
	@Requires("text != null")
	public boolean trySetText(String text)
	{
		if (_text.equals(text))
			return true;
		
		if (isValidText(text))
		{
			_text = text;
			fireObjectChangedEvent();
			return true;
		}
		else
			return false;
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
		_LOG.fine("ID set to " + id);
		fireObjectChangedEvent();
	}
	
	private boolean isValidText(String input)
	{
		if (_textValidator == null)
			return true;
		else
			return _textValidator.isValid(input);
	}
	
	/**
	 * The validator that can be set with this method will be used
	 * to validate input which is given via the {@link #trySetText(String) trySetText} method.
	 * If <code>null</code> is given, input will not be validated.
	 */
	public void setTextValidator(Validator<String> validator)
	{
		_textValidator = validator;
	}
	
	@Override
	public String toString()
	{
		return "Word: '"+ getText() + "', ID: '" + _id + "'";
	}
}
