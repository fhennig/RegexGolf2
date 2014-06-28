package regexgolf2.controllers;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import regexgolf2.model.SolvableChallenge;

import com.google.java.contract.Ensures;

/**
 * This Interface can be implemented by Controllers to indicate that they
 * contain one or more Challenges. The Interface also includes a BooleanProperty
 * that indicates if the Challenge should be editable.
 */
public interface ChallengeContainer
{
	/**
	 * A Property that contains the currently selected or only challenge in this
	 * ChallengeContainer. The property may contain null.
	 */
	@Ensures("result != null")
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty();

	/**
	 * This Property should inicate if the challenge should be editable.
	 */
	@Ensures("result != null")
	public ReadOnlyBooleanProperty editableProperty();
}
