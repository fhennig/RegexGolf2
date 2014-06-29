package regexgolf2.model.regexgenerator;

import java.util.Collections;
import java.util.List;

import regexgolf2.model.Challenge;
import regexgolf2.model.Requirement;
import regexgolf2.model.Word;

public class RandomGenerator extends AbstractGenerator
{
	private static final String _NAME = "Random Generator";

	
	
	public RandomGenerator()
	{
		super(_NAME);
	}

	
	
	@Override
	public Challenge generateChallenge(List<Word> words)
	{
		Collections.shuffle(words);
		
		Challenge challenge = new Challenge();
		challenge.setName("Generated Challenge");
		
		for (int i = 0; i < 14; i++)
		{
			if (words.size() <= i) //Not enough words
				break;
			
			boolean expMatchRes = (i % 2) == 0;
			
			Requirement req = new Requirement(expMatchRes, words.get(i).getText());
			
			challenge.addRequirement(req);
		}
		
		return challenge;
	}
}
