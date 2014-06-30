package regexgolf2.model;

import com.google.java.contract.Requires;

public class ScoreCalculator
{
	private final SolvableChallenge _challenge;
	private int _score;
	
	
	
	@Requires("challenge != null")
	public ScoreCalculator(SolvableChallenge challenge)
	{
		_challenge = challenge;
		recalculate();
		_challenge.addObjectChangedListener(e -> recalculate());
	}
	
	
	
	private void recalculate()
	{
		_score = _challenge.getSolution().getSolution().length();
	}
	
	public int getScore()
	{
		return _score;
	}
}
