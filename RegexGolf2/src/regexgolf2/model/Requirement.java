package regexgolf2.model;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Requirement extends ObservableObject
{
	private final boolean _expectedMatchResult;
	private String _word = "";
	
	
	
	/**
	 * Initializes a Requirement with the given expectedMatchResult
	 * and an empty String for the Word.
	 */
	public Requirement(boolean expectedMatchResult)
	{
		this(expectedMatchResult, "");
	}
	
	@Requires("word != null")
	public Requirement(boolean expectedMatchResult, String word)
	{
		_expectedMatchResult = expectedMatchResult;
		_word = word;
	}
	
	
	
	@Ensures("result != null")
	public String getWord()
	{
		return _word;
	}
	
	@Requires("word != null")
	public void setWord(String word)
	{
		if (_word.equals(word))
			return;
		_word = word;
		fireObjectChangedEvent();
	}
	
	public boolean getExpectedMatchResult()
	{
		return _expectedMatchResult;
	}
	
	public boolean applySolution(Solution solution)
	{
		boolean matchResult = solution.getPattern().matcher(getWord()).matches();
		return _expectedMatchResult == matchResult;
	}
	
	@Override
	public String toString()
	{
		return getWord();
	}
}
