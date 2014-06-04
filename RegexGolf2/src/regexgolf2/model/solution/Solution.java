package regexgolf2.model.solution;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import regexgolf2.model.ObservableObject;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;


public class Solution extends ObservableObject
{
	private Pattern _regex = Pattern.compile("");
	
	
	
	public Solution() { }
	
	@Requires("regex != null")
	public Solution(String regex)
	{
		trySetSolution(regex);
	}
	
	
	
	@Requires("regex != null")
	public boolean trySetSolution(String regex)
	{
		try
		{
			_regex = Pattern.compile(regex);
			fireObjectChangedEvent();
			return true;
		}
		catch (PatternSyntaxException pse)
		{
			return false;
		}
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
}
