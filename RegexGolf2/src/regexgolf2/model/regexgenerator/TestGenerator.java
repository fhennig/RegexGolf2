package regexgolf2.model.regexgenerator;

import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Word;
import regexgolf2.startup.ChallengeFactory;

public class TestGenerator implements Generator
{
	private static final String _NAME = "Test Generator";
		
	
	
	@Override
	public Challenge generateChallenge(List<Word> words)
	{
		return ChallengeFactory.getRandomChallenge();
	}

	@Override
	public String getName()
	{
		return _NAME;
	}
}
