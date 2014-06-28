package regexgolf2.services.challengegenerator;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.startup.ChallengeFactory;

public class ChallengeGeneratorService
{
	public SolvableChallenge generateChallenge()
	{
		Challenge c = ChallengeFactory.getRandomChallenge();
		SolvableChallenge sc = new SolvableChallenge(new Solution(), c);
		
		return sc;
	}
}
