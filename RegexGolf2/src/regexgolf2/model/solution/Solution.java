package regexgolf2.model.solution;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import regexgolf2.model.ObservableObject;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;


public class Solution extends ObservableObject
{
	private Pattern _regex = Pattern.compile("");
	private int _id;
	
	
	
	public Solution() { }
	
	@Requires("regex != null")
	public Solution(String regex)
	{
		trySetSolution(regex);
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
	
	@Requires("regex != null")
	public boolean trySetSolution(String regex)
	{
		if (getSolution().equals(regex))
			return true;
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
