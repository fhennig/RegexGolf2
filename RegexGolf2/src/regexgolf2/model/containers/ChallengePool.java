package regexgolf2.model.containers;

import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.saving.Savable;
import regexgolf2.services.persistence.saving.SaveVisitor;

import com.google.java.contract.Ensures;

public class ChallengePool extends Container<SolvableChallenge> implements Savable
{
	@Ensures("result != null")
	public SolvableChallenge createNew()
	{
		SolvableChallenge c = new SolvableChallenge();
		add(c);
		return c;
	}

	@Override
	public void accept(SaveVisitor visitor) throws PersistenceException
	{
		visitor.visit(this);
	}
}
