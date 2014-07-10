package regexgolf2.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.stage.Window;

import javax.swing.JOptionPane;

import regexgolf2.model.ObjectChangedListener;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.model.regexgenerator.Generator;
import regexgolf2.services.ChangeTrackingService;
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
	private final GeneratorConfigUICreator _configUICreator = new GeneratorConfigUICreator();

	private final ObjectProperty<SolvableChallenge> _challenge = new SimpleObjectProperty<SolvableChallenge>();

	private final ChallengeGeneratorService _generatorService;
	private final ChangeTrackingService _changeTrackingService;
	private final ChallengeRepository _challengeRepository;

	private final ObjectChangedListener _challengeListener;
	private final ObjectChangedListener _persStateListener;
	private boolean _isSaved = false;



	@Requires("services != null")
	public ChallengeGeneratorController(ServiceContainer services, Window parent)
			throws IOException
	{
		_generatorService = services.getGeneratorService();
		_changeTrackingService = services.getChangeTrackingService();
		_challengeRepository = services.getChallengeRepository();

		WordRepositoryController wrc = new WordRepositoryController(services.getWordRepository());

		_ui = new ChallengeGeneratorUI(parent);
		_ui.setWordRepositoryPanel(wrc.getUINode());
		_ui.getGenerateButton().setOnAction(e -> generateButtonClicked());
		// ChoiceBox
		_ui.getGeneratorChoiceBox().getSelectionModel().selectedItemProperty()
				.addListener((o, oV, nV) -> choiceBoxSelectionChanged(nV));
		initChoiceBoxItems();
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

	private void challengePropertyChanged(SolvableChallenge oldChallenge,
			SolvableChallenge newChallenge)
	{
		if (oldChallenge != null)
		{
			oldChallenge.removeObjectChangedListener(_challengeListener);
			Optional.ofNullable(_changeTrackingService.getPersistenceState(oldChallenge))
					.ifPresent(ps -> ps.removeObjectChangedListener(_persStateListener));
			_isSaved = false;
			persistenceStateChanged();
		}
		if (newChallenge != null)
		{
			newChallenge.addObjectChangedListener(_challengeListener);
			_ui.getChallengeNameTextField().setText(getChallenge().getChallenge().getName());
		}
	}

	private void challengeChanged()
	{
		_ui.getChallengeNameTextField().setText(getChallenge().getChallenge().getName());
	}

	private void persistenceStateChanged()
	{
		boolean isChanged;
		if (_isSaved)
			// If it is saved to the DB, get ChangeState from Repository
			isChanged = _changeTrackingService.getPersistenceState(getChallenge()).isChanged();
		else
			isChanged = true;
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
				_changeTrackingService.getPersistenceState(getChallenge())
						.addObjectChangedListener(_persStateListener);
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
		Generator gen = selectedItem.getGenerator();
		_generatorService.setSelectedGenerator(gen);
		_ui.setGeneratorConfigPaneContent(_configUICreator.getUIFor(gen.getConfig()));
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
