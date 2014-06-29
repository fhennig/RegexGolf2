package regexgolf2.controllers;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.stage.Window;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.regexgenerator.Generator;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.initializing.ServiceContainer;
import regexgolf2.ui.challengegenerator.ChallengeGeneratorUI;
import regexgolf2.ui.challengegenerator.GeneratorItem;

import com.google.java.contract.Requires;

public class ChallengeGeneratorController implements ChallengeContainer
{
	private static final ReadOnlyBooleanProperty _EDITABLE = new ReadOnlyBooleanWrapper(false);

	private ChallengeGeneratorUI _ui;

	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<SolvableChallenge>();

	private final ChallengeGeneratorService _generatorService;

	
	
	@Requires("services != null")
	public ChallengeGeneratorController(ServiceContainer services, Window parent)
			throws IOException
	{
		_generatorService = services.getGeneratorService();

		WordRepositoryController wrc = new WordRepositoryController(
				services.getWordRepository());

		_ui = new ChallengeGeneratorUI(parent);
		_ui.setWordRepositoryPanel(wrc.getUINode());
		_ui.getGenerateButton().setOnAction(e -> generateButtonClicked());
		initChoiceBoxItems();
		_ui.getGeneratorChoiceBox().getSelectionModel().selectedItemProperty()
				.addListener((o, oV, nV) -> choiceBoxSelectionChanged(nV));
	}

	
	
	private void initChoiceBoxItems()
	{
		for (Generator g : _generatorService.getGenerators())
		{
			GeneratorItem item = new GeneratorItem(g);
			_ui.getGeneratorChoiceBox().getItems().add(item);
			if (_generatorService.getSelectedGenerator() == g)
				_ui.getGeneratorChoiceBox().getSelectionModel().select(item);
		}
	}

	private void generateButtonClicked()
	{
		SolvableChallenge c = _generatorService.generateChallenge();
		setChallenge(c);
	}

	private void choiceBoxSelectionChanged(GeneratorItem selectedItem)
	{
		_generatorService.setSelectedGenerator(selectedItem.getGenerator());
	}

	/**
	 * Sets the selected Challenge
	 */
	private void setChallenge(SolvableChallenge challenge)
	{
		_challenge.set(challenge);
	}

	@Override
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _challenge;
	}

	@Override
	public ReadOnlyBooleanProperty editableProperty()
	{
		return _EDITABLE;
	}

	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
