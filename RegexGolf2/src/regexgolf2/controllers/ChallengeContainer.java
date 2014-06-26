package regexgolf2.controllers;

import javafx.beans.property.ReadOnlyObjectProperty;
import regexgolf2.model.SolvableChallenge;

import com.google.java.contract.Ensures;

public interface ChallengeContainer
{
	/**
	 * A Property that contains the currently selected or only
	 * challenge in this ChallengeContainer.
	 * The property may contain null.
	 */
	@Ensures("result != null")
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty();
}
