package regexgolf2.controllers;

import com.google.java.contract.Requires;

import regexgolf2.services.challengerepository.ChallengeRepository;

public class MainController
{
	private final ChallengeRepository _challenges;
	
	@Requires("challenges != null")
	public MainController(ChallengeRepository challenges)
	{
		_challenges = challenges;
		_challenges.getAll();
	}
	
	//TODO ChallengeRepository.ChallengesChangedEvent
	//TODO challengeRepositoryController (save-, delete-, new-Buttons)
	//TODO challengeSelectionController (Table with Challenges)
	//TODO challengeSolvingController
	//TODO challengeEditingController
}
