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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.stage.Window;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.repositories.ChallengeRepository;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.ui.modules.ModulesUI;

import com.google.java.contract.Requires;

public class ModulesController
{
	private ModulesUI _ui;

	private final ChallengeRepositoryController _challengeRepoController;
	private final WordRepositoryController _wordRepositoryController;
	private final ChallengeGeneratorController _challengeGeneratorController;
	
	private final ObjectProperty<SolvableChallenge> _selectedChallenge = new SimpleObjectProperty<>();
	private final BooleanProperty _editableProperty = new SimpleBooleanProperty();
    
    private Map<Tab, ChallengeContainer> _challengeContainerMap = new HashMap<>();
	
	
	
	@Requires({
		"challengeRepository != null",
		"wordRepository != null"
	})
	public ModulesController(ChallengeRepository challengeRepository, WordRepository wordRepository, Window parent) throws IOException
	{
		_challengeRepoController = new ChallengeRepositoryController(challengeRepository);
		_wordRepositoryController = new WordRepositoryController(wordRepository);
		_challengeGeneratorController = new ChallengeGeneratorController();
		
		initUI(parent);
		initSelectedTabHandler();
		initChallengePropertyBinding();
	}
	
	
	
	/**
	 * This needs to be done once. When the selected Tab changes,
	 * the Handler will take care of the binding.
	 */
	private void initChallengePropertyBinding()
	{
		refreshBindings(_challengeContainerMap.get(_ui.getSelectedTab()));
	}
	
	private void initUI(Window parent) throws IOException
	{
		_ui = new ModulesUI(parent);
		_ui.setChallengeRepoPanelContent(_challengeRepoController.getUINode());
		_ui.setWordRepositoryPanel(_wordRepositoryController.getUINode());
		_ui.setChallengeGeneratorPanel(_challengeGeneratorController.getUINode());
		
		_challengeContainerMap.put(_ui.getSavedChallengesTab(), _challengeRepoController);
		_challengeContainerMap.put(_ui.getChallengeGeneratorTab(), _challengeGeneratorController);
	}
	
	private void initSelectedTabHandler()
	{
		_ui.selectedTabProperty().addListener(new ChangeListener<Tab>()
		{
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue,
					Tab newValue)
			{
				refreshBindings(_challengeContainerMap.get(newValue));
			}
		});
	}
	
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
