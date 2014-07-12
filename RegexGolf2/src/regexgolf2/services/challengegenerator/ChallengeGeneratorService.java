package regexgolf2.services.challengegenerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import regexgolf2.model.Challenge;
import regexgolf2.model.Solution;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.Word;
import regexgolf2.model.challengegenerator.Generator;
import regexgolf2.model.challengegenerator.RandomGenerator;
import regexgolf2.model.challengegenerator.TestGenerator;
import regexgolf2.model.containers.WordPool;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeGeneratorService
{
	private final WordPool _wordPool;
	private Set<Generator> _generators = new HashSet<>();
	private Generator _selectedGenerator;
	
	
	
	@Requires("wordPool != null")
	public ChallengeGeneratorService(WordPool wordPool)
	{
		_wordPool = wordPool;
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
		return _wordPool.stream()
		.filter(word -> true) //TODO filter properly
		.collect(Collectors.toList());	
	}
}
