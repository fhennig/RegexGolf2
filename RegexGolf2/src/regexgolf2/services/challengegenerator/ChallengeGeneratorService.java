package regexgolf2.services.challengegenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.Word;
import regexgolf2.model.regexgenerator.Generator;
import regexgolf2.model.regexgenerator.RandomGenerator;
import regexgolf2.model.regexgenerator.TestGenerator;
import regexgolf2.services.repositories.WordRepository;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeGeneratorService
{
	private final WordRepository _wordRepository;
	private Set<Generator> _generators = new HashSet<>();
	private Generator _selectedGenerator;
	
	
	
	@Requires("wordRepository != null")
	public ChallengeGeneratorService(WordRepository wordRepository)
	{
		_wordRepository = wordRepository;
		initGenerators();
	}
	
	
	
	private void initGenerators()
	{
		Generator g = new TestGenerator();
		_generators.add(g);
		g = new RandomGenerator();
		_generators.add(g);
		_selectedGenerator = g;
	}
	
	/**
	 * Returns a Set of available Generators.
	 * {@link #setSelectedGenerator(Generator)} takes only these Generators
	 * as valid arguments.
	 */
	public Set<Generator> getGenerators()
	{
		return Collections.unmodifiableSet(_generators);
	}
	
	@Requires("getGenerators().contains(generator)")
	public void setSelectedGenerator(Generator generator)
	{
		_selectedGenerator = generator;
	}
	
	@Ensures("result != null")
	public Generator getSelectedGenerator()
	{
		return _selectedGenerator;
	}
	
	public SolvableChallenge generateChallenge()
	{
		Challenge c = getSelectedGenerator().generateChallenge(getFilteredWords());
		SolvableChallenge sc = new SolvableChallenge(new Solution(), c);
		
		return sc;
	}
	
	private List<Word> getFilteredWords()
	{
		List<Word> words = new ArrayList<>();
		
		for (Word word : _wordRepository.getAll())
		{
			//TODO filter words
			words.add(word);
		}
		
		return words;
	}
}
