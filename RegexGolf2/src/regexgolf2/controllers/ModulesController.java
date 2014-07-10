package regexgolf2.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.stage.Window;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.services.Services;
import regexgolf2.ui.modules.ModulesUI;

import com.google.java.contract.Requires;

public class ModulesController
{
	private final ModulesUI _ui;

	private final ObjectProperty<SolvableChallenge> _selectedChallenge = new SimpleObjectProperty<>();
	private final BooleanProperty _editableProperty = new SimpleBooleanProperty();

	/**
	 * This map maps the Tabs in the TabPane to the contained Controllers. This
	 * is used to synchronize to currently displayed challenge with the selected
	 * Tab.
	 */
	private final Map<Tab, ChallengeContainer> _challengeContainerMap = new HashMap<>();



	@Requires("services != null")
	public ModulesController(Services services, Window parent) throws IOException
	{
		// Init child Controllers
		ChallengeRepositoryController challengeRepoController = new ChallengeRepositoryController(
				services.getPersistenceService());
		ChallengeGeneratorController challengeGeneratorController = new ChallengeGeneratorController(
				services, parent);

		// Init UI
		_ui = new ModulesUI();
		_ui.setChallengeRepoPanelContent(challengeRepoController.getUINode());
		_ui.setChallengeGeneratorPanel(challengeGeneratorController.getUINode());

		// Init ChallengeContainerMap
		_challengeContainerMap.put(_ui.getSavedChallengesTab(), challengeRepoController);
		_challengeContainerMap.put(_ui.getChallengeGeneratorTab(), challengeGeneratorController);

		// Add Handler to refresh Bindings if selected Tab changes
		_ui.selectedTabProperty().addListener(
				(o, oV, newValue) -> refreshBindings(_challengeContainerMap.get(newValue)));

		// Call once to initialize
		refreshBindings(_challengeContainerMap.get(_ui.getSelectedTab()));
	}



	/**
	 * Refreshes the selected Challenge and the editable Property.
	 * 
	 * @param selectedContainer
	 *            The selected component that should be used for the binding.
	 */
	@Requires("selectedContainer != null")
	private void refreshBindings(ChallengeContainer selectedContainer)
	{
		_selectedChallenge.unbind();
		_selectedChallenge.bind(selectedContainer.challengeProperty());

		_editableProperty.unbind();
		_editableProperty.bind(selectedContainer.editableProperty());
	}

	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _selectedChallenge;
	}

	public ReadOnlyBooleanProperty editableProperty()
	{
		return _editableProperty;
	}

	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
