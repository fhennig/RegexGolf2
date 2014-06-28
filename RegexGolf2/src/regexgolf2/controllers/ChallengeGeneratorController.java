package regexgolf2.controllers;

import java.io.IOException;

import com.google.java.contract.Requires;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.ui.challengegenerator.ChallengeGeneratorUI;

public class ChallengeGeneratorController implements ChallengeContainer
{
	private static final ReadOnlyBooleanProperty _editable = new ReadOnlyBooleanWrapper(false);
	
	private ChallengeGeneratorUI _ui;
	
	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<SolvableChallenge>();
	
	private final ChallengeGeneratorService _generatorService;
	
	
	
	@Requires("service != null")
	public ChallengeGeneratorController(ChallengeGeneratorService service) throws IOException
	{
		_generatorService = service;
		initUI();
		initGenerateButtonHandler();
	}

	
	
	private void initUI() throws IOException
	{
		_ui = new ChallengeGeneratorUI();
	}
	
	private void initGenerateButtonHandler()
	{
		_ui.getGenerateButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				SolvableChallenge c = _generatorService.generateChallenge();
				setChallenge(c);
			}
		});
	}
	
	private void setChallenge(SolvableChallenge challenge)
	{
		_challenge.set(challenge);
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
