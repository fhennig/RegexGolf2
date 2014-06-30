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
		_score = calculateScore();
	}

	private int calculateScore()
	{
		double solutionLength = _challenge.getSolution().getSolution().length();
		double amountRequirements = _challenge.getAmountRequirements();
		double amtSolvedReq = _challenge.getAmountCompliedRequirements();
		double unsolvedFactor = (1 / (1 + Math.pow((amountRequirements - amtSolvedReq), 2)));

		if (solutionLength == 0)
			return 0;

		int score = (int) ((1d / solutionLength) * amtSolvedReq * amountRequirements * 100 * unsolvedFactor);
		return score;
	}

	public int getScore()
	{
		return _score;
	}
}
