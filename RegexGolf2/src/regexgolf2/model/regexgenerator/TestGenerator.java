package regexgolf2.model.regexgenerator;

import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Word;
import regexgolf2.startup.ChallengeFactory;

public class TestGenerator extends AbstractGenerator
{
	private static final String _NAME = "Test Generator";
		


	public TestGenerator()
	{
		super(_NAME);
	}

	
	
	@Override
	public Challenge generateChallenge(List<Word> words)
	{
		return ChallengeFactory.getRandomChallenge();
	}
}
