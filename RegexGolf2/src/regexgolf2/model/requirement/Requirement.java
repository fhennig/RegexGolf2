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
	private final List<MatchResultChangedListener> _matchResultChangedListeners = new ArrayList<>();
	private final boolean _expectedMatchResult;
	private boolean _matchResult;
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
	
	public boolean isComplied()
	{
		return _matchResult == _expectedMatchResult;
	}
	
	public void applySolution(Solution solution)
	{
		boolean newMatchResult = solution.getPattern().matcher(getWord()).matches();
		_matchResult = newMatchResult;
		fireMatchResultChangedEvent();
	}
	
	private void fireWordChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (WordChangedListener listener : _wordChangedListeners)
		{
			listener.wordChanged(event);
		}
	}
	
	private void fireMatchResultChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (MatchResultChangedListener listener : _matchResultChangedListeners)
		{
			listener.matchResultChanged(event);
		}
	}
	
	@Requires("listener != null")
	public void addWordChangedListener(WordChangedListener listener)
	{
		_wordChangedListeners.add(listener);
	}
	
	@Requires("listener != null")
	public void addMatchResultChangedListener(MatchResultChangedListener listener)
	{
		_matchResultChangedListeners.add(listener);
	}
}
