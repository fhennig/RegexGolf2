package regexgolf2.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.stage.Window;
import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.regexgenerator.Generator;
import regexgolf2.services.challengegenerator.ChallengeGeneratorService;
import regexgolf2.services.initializing.ServiceContainer;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.ui.challengegenerator.ChallengeGeneratorUI;
import regexgolf2.ui.challengegenerator.GeneratorItem;

import com.google.java.contract.Requires;

public class ChallengeGeneratorController implements ChallengeContainer
{
	private static final ReadOnlyBooleanProperty _EDITABLE = new ReadOnlyBooleanWrapper(false);

	private ChallengeGeneratorUI _ui;

	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<SolvableChallenge>();

	private final ChallengeGeneratorService _generatorService;
	private final ChallengeRepository _challengeRepository;

	private final ObjectChangedListener _challengeListener;
	private final ObjectChangedListener _persStateListener;
	private boolean _isSaved = false;



	@Requires("services != null")
	public ChallengeGeneratorController(ServiceContainer services, Window parent)
			throws IOException
	{
		_generatorService = services.getGeneratorService();
		_challengeRepository = services.getChallengeRepository();

		WordRepositoryController wrc = new WordRepositoryController(services.getWordRepository());

		_ui = new ChallengeGeneratorUI(parent);
		_ui.setWordRepositoryPanel(wrc.getUINode());
		_ui.getGenerateButton().setOnAction(e -> generateButtonClicked());
		// ChoiceBox
		initChoiceBoxItems();
		_ui.getGeneratorChoiceBox().getSelectionModel().selectedItemProperty()
				.addListener((o, oV, nV) -> choiceBoxSelectionChanged(nV));
		// ChallengeNameTextField
		_ui.getChallengeNameTextField().textProperty()
				.addListener((o, oV, nV) -> challengeNameTextFieldChanged(nV));
		// Save Button
		_ui.getSaveButton().setOnAction(e -> saveButtonClicked());

		// ChallengeProperty Listener
		_challenge.addListener((o, oV, nV) -> challengePropertyChanged(oV, nV));
		// Listener for the Challenge itself
		_challengeListener = e -> challengeChanged();
		// Listener for the PersistenceState of the Challenge
		_persStateListener = e -> persistenceStateChanged();
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

	private void challengePropertyChanged(SolvableChallenge oldValue, SolvableChallenge newValue)
	{
		if (oldValue != null)
		{
			oldValue.removeObjectChangedListener(_challengeListener);
			_challengeRepository.getPersistenceState(oldValue).removeObjectChangedListener(_persStateListener);
		}
		if (newValue != null)
		{
			newValue.addObjectChangedListener(_challengeListener);
			_ui.getChallengeNameTextField().setText(getChallenge().getChallenge().getName());
		}
	}

	private void challengeChanged()
	{
		_ui.getChallengeNameTextField().setText(getChallenge().getChallenge().getName());
	}

	private void persistenceStateChanged()
	{
		boolean isChanged = _challengeRepository.getPersistenceState(getChallenge()).isNew();
		_ui.getSaveButton().setDisable(!isChanged);
	}

	private void saveButtonClicked()
	{
		if (getChallenge() == null)
			return;
		try
		{
			_challengeRepository.save(getChallenge());
			if (!_isSaved)
			{
				_isSaved = true;
				_challengeRepository.getPersistenceState(getChallenge()).addObjectChangedListener(_persStateListener);
				persistenceStateChanged();
			}
		} catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Error with Database");
		}
	}

	private void challengeNameTextFieldChanged(String newText)
	{
		if (getChallenge() == null)
			return;
		getChallenge().getChallenge().setName(newText);
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

	public SolvableChallenge getChallenge()
	{
		return _challenge.get();
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
