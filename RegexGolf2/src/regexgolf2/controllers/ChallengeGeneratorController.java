package regexgolf2.controllers;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;
import regexgolf2.model.SolvableChallenge;

public class ChallengeGeneratorController implements ChallengeContainer
{

	@Override
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty()
	{
		// TODO create bindable property
		return null;
	}
	
	public Node getUINode()
	{
		//TODO call UI class
		return null;
	}
}
