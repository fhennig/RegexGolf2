package regexgolf2.services.challengesolvingservice;

import java.util.List;

import regexgolf2.model.requirement.Requirement;

public interface ChallengeSolvingService
{
	void setSolution(String regex);
	
	String getSolution();
	
	List<Requirement> getRequirements(boolean expectedMatchResult);
	
	boolean isComplied(Requirement requirement);
	
	int getAmountRequirements();
	
	int getAmountCompliedRequirements();
}
