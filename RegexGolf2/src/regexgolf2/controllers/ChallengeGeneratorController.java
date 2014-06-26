package regexgolf2.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.ui.challengegenerator.ChallengeGeneratorUI;

public class ChallengeGeneratorController implements ChallengeContainer
{
	private static final ReadOnlyBooleanProperty _editable = new ReadOnlyBooleanWrapper(false);
	
	private ChallengeGeneratorUI _ui;
	
	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<SolvableChallenge>();
	
	
	
	public ChallengeGeneratorController()
	{
		initUI();
	}

	
	
	private void initUI()
	{
		_ui = new ChallengeGeneratorUI();
	}
	
	@Override
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _challenge;
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}

	@Override
	public ReadOnlyBooleanProperty editableProperty()
	{
		return _editable;
	}
}
