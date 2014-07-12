package regexgolf2.model.challengegenerator;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class RandomGeneratorConfig implements GeneratorConfig
{
	private int _doMatchLowerBound = 5;
	private int _doMatchHigherBound = 10;
	private int _dontMatchLowerBound = 5;
	private int _dontMatchHigherBound = 10;
	


	@Ensures("result >= 0")
	public int getDoMatchLowerBound()
	{
		return _doMatchLowerBound;
	}

	@Requires({
		"doMatchLowerBound >= 0",
		"doMatchLowerBound <= getDoMatchHigherBound()"
	})
	public void setDoMatchLowerBound(int doMatchLowerBound)
	{
		_doMatchLowerBound = doMatchLowerBound;
	}

	@Ensures("result >= 0")
	public int getDoMatchHigherBound()
	{
		return _doMatchHigherBound;
	}

	@Requires({
		"doMatchHigherBound >= getDoMatchLowerBound()"
	})
	public void setDoMatchHigherBound(int doMatchHigherBound)
	{
		_doMatchHigherBound = doMatchHigherBound;
	}
	
	@Ensures("result >= 0")
	public int getDontMatchLowerBound()
	{
		return _dontMatchLowerBound;
	}

	@Requires({
		"dontMatchLowerBound >= 0",
		"dontMatchLowerBound <= getDontMatchHigherBound()"
	})
	public void setDontMatchLowerBound(int dontMatchLowerBound)
	{
		_dontMatchLowerBound = dontMatchLowerBound;
	}

	@Ensures("result >= 0")
	public int getDontMatchHigherBound()
	{
		return _dontMatchHigherBound;
	}

	@Requires({
		"dontMatchHigherBound >= getDontMatchLowerBound()"
	})
	public void setDontMatchHigherBound(int dontMatchHigherBound)
	{
		_dontMatchHigherBound = dontMatchHigherBound;
	}	
	
	@Override
	public void accept(GeneratorConfigVisitor visitor)
	{
		visitor.visit(this);
	}
}
