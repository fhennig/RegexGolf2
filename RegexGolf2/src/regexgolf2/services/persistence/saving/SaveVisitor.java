package regexgolf2.services.persistence.saving;

import regexgolf2.model.ChallengePool;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.PersistenceException;

public interface SaveVisitor
{
	void visit(ChallengePool challengePool) throws PersistenceException;

	void visit(SolvableChallenge solvableChallenge) throws PersistenceException;
}
