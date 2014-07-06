package regexgolf2.model.regexgenerator;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;
import regexgolf2.model.Word;
import regexgolf2.util.Util;

public class RandomGenerator implements Generator
{
	private static final Logger _LOG = Logger.getLogger(RandomGenerator.class.getName());
	private static final String _NAME = "Random Generator";
	private final RandomGeneratorConfig _config = new RandomGeneratorConfig();

	
	
	@Override
	public Challenge generateChallenge(List<Word> words)
	{
		Collections.shuffle(words);
		
		Challenge challenge = new Challenge();
		challenge.setName("Generated Challenge");
		
		int amtDoMatchWords = getAmountDoMatchWords();
		int amtDontMatchWords = getAmountDontMatchWords();
		
		for (int i = 0; i < amtDoMatchWords; i++)
		{
			if (words.size() <= i) //Not enough words
				break;
			
			Requirement req = new Requirement(true, words.get(i).getText());
			
			challenge.addRequirement(req);
		}
		
		for (int i = amtDoMatchWords; i < amtDontMatchWords + amtDoMatchWords; i++)
		{
			if (words.size() <= i)
				break;
			

			Requirement req = new Requirement(false, words.get(i).getText());
			
			challenge.addRequirement(req);
		}
		
		return challenge;
	}
	
	private int getAmountDoMatchWords()
	{
		int l = _config.getDoMatchLowerBound();
		int h = _config.getDoMatchHigherBound();
		int result = Util.randInt(l, h);
		_LOG.fine("Generated Amount of Do Match Words: " + result);
		return result;
	}
	
	private int getAmountDontMatchWords()
	{
		int l = _config.getDontMatchLowerBound();
		int h = _config.getDontMatchHigherBound();
		int result = Util.randInt(l, h);
		_LOG.fine("Generated Amount of Don't Match Words: " + result);
		return result;
	}
	
	@Override
	public String getName()
	{
		return _NAME;
	}
	
	@Override
	public GeneratorConfig getConfig()
	{
		return _config;
	}
}
