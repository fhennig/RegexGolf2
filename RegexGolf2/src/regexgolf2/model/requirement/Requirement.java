package regexgolf2.model.requirement;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import regexgolf2.model.solution.Solution;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Requirement
{
	private final List<WordChangedListener> _wordChangedListeners = new ArrayList<>();
	private final boolean _expectedMatchResult;
	private String _word = "";
	
	public Requirement(boolean expectedMatchResult, String word)
	{
		_expectedMatchResult = expectedMatchResult;
		setWord(word);
	}
	
	@Requires("word != null")
	public void setWord(String word)
	{
		_word = word;
		fireWordChangedEvent();
	}
	
	@Ensures("result != null")
	public String getWord()
	{
		return _word;
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
	
	private void fireWordChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (WordChangedListener listener : _wordChangedListeners)
		{
			listener.wordChanged(event);
		}
	}
	
	@Requires("listener != null")
	public void addWordChangedListener(WordChangedListener listener)
	{
		_wordChangedListeners.add(listener);
	}
}
