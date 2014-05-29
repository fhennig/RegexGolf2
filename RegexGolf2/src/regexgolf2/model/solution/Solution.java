package regexgolf2.model.solution;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Pattern;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;


public class Solution
{
	private final List<SolutionChangedListener> _listeners = new ArrayList<>();
	private Pattern _regex = Pattern.compile("");
	
	
	@Requires("regex != null")
	public void setSolution(String regex)
	{
		_regex = Pattern.compile(regex);
		fireSolutionChangedEvent();
	}
	
	@Ensures("result != null")
	public String getSolution()
	{
		return _regex.toString();
	}
	
	@Ensures("result != null")
	public Pattern getPattern()
	{
		return _regex;
	}
	
	private void fireSolutionChangedEvent()
	{
		EventObject event = new EventObject(this);
		
		for (SolutionChangedListener listener : _listeners)
		{
			listener.solutionChanged(event);
		}
	}
	
	@Requires("listener != null")
	public void addSolutionChangedListener(SolutionChangedListener listener)
	{
		_listeners.add(listener);
	}
}
