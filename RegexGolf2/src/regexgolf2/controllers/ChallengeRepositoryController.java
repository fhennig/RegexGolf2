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
import javafx.scene.Node;

import javax.swing.JOptionPane;

import regexgolf2.model.ChallengePool;
import regexgolf2.model.ContainerChangedEvent;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.persistence.PersistenceException;
import regexgolf2.services.persistence.PersistenceService;
import regexgolf2.ui.challengerepositoryview.ChallengeRepositoryUI;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeRepositoryController implements ChallengeContainer
{
	private final ChallengePool _challengePool;
	private final PersistenceService _persistenceService;
	private final ChallengeRepositoryUI _ui;
	private final Map<ChallengeItem, ChallengeItemController> _itemControllers = new HashMap<>();
	private final ObjectProperty<SolvableChallenge> _selectedChObjectProperty = new SimpleObjectProperty<>();
	private final BooleanProperty _editmodeProperty = new SimpleBooleanProperty();



	@Requires("persistenceService != null")
	public ChallengeRepositoryController(PersistenceService persistenceService) throws IOException
	{
		_challengePool = persistenceService.getChallengePool();
		_persistenceService = persistenceService;
		_ui = new ChallengeRepositoryUI();

		initButtonBindings();
		initListeners();
		initAddButtonHandler();
		initRemoveButtonHandler();
		initSaveButtonHandler();
		_challengePool.forEach(challenge -> addItem(challenge));
	}



	private void initButtonBindings()
	{
		_ui.getRemoveButton().disableProperty().bind(_selectedChObjectProperty.isNull());
		_ui.getEditButton().disableProperty().bind(_selectedChObjectProperty.isNull());

		_ui.getEditButton().selectedProperty().bindBidirectional(_editmodeProperty);
	}

	private void initListeners()
	{
		_ui.selectedChallengeProperty().addListener(
				(ChangeListener<ChallengeItem>) (observable, oldValue, newValue) ->
				{
					if (newValue != null)
						// Get the Challenge that corresponds to the Item
						_selectedChObjectProperty
								.set(_itemControllers.get(newValue).getChallenge());
					else
						_selectedChObjectProperty.set(null);

					// If the selected challenge changed, the edit mode is
					// resetted
					// to false
					setEditmode(false);
				});

		_challengePool.addListener(event -> refreshListViewItemList(event));
	}

	private void initAddButtonHandler()
	{
		_ui.getAddButton().setOnAction(arg0 ->
		{
			SolvableChallenge newChallenge = _challengePool.createNew();
			selectChallenge(newChallenge);
			setEditmode(true);
		});
	}

	private void initRemoveButtonHandler()
	{
		_ui.getRemoveButton()
				.setOnAction(
						evt ->
						{
							ChallengeItem item = _ui.getSelectedChallengeItem();
							assert item != null : "Remove Button was clicked by no challenge was selected.";

							SolvableChallenge challenge = _itemControllers.get(item).getChallenge();
							assert _challengePool.contains(challenge) : "The challenge to remove is not contained in the ChallengeRepo.";
							_challengePool.remove(challenge);
						});
	}

	private void initSaveButtonHandler()
	{
		_ui.getSaveButton().setOnAction(arg0 ->
		{
			try
			{
				_persistenceService.save(_challengePool);
			} catch (PersistenceException e)
			{
				JOptionPane.showMessageDialog(null, "Something fucked up");
			}
		});
	}

	/**
	 * Synchronizes the List of Challenges in the UI with the List of Challenges
	 * in the Repository. This method ensures, that for every Challenge in the
	 * Repository, there exists an item in the UI. This method is called from
	 * the ServiceChangedListener that listens to the Repository.
	 */
	private void refreshListViewItemList(ContainerChangedEvent<? extends SolvableChallenge> event)
	{
		if (event.getRemovedItem() != null)
			removeItemFor(event.getRemovedItem());

		if (event.getAddedItem() != null)
			addItem(event.getAddedItem());
	}

	private void removeItemFor(SolvableChallenge challenge)
	{
		_ui.getChallengeItemList().stream()
			.filter(item -> _itemControllers.get(item).getChallenge().equals(challenge))
			.findFirst()
			.ifPresent(item -> {
				_ui.getChallengeItemList().remove(item);
				_itemControllers.get(item).discard();
				_itemControllers.remove(item);
			});
	}

	/**
	 * Initializes a new Controller with the given Challenge and puts the
	 * Controller in the map. The Item controlled by the Controller is added to
	 * the UI list.
	 * 
	 * @return The item that was added to the UI.
	 */
	private ChallengeItem addItem(SolvableChallenge challenge)
	{
		ChallengeItemController controller = new ChallengeItemController(challenge,
				_persistenceService.getPersistenceState(challenge));
		_ui.getChallengeItemList().add(controller.getItem());
		_itemControllers.put(controller.getItem(), controller);
		return controller.getItem();
	}

	/**
	 * Selects the ChallengeItem in the UI for a given SolvableChallenge. This
	 * is used to immediately select a newly created Challenge.
	 * 
	 * @param challenge
	 *            the
	 */
	private void selectChallenge(SolvableChallenge challenge)
	{
		for (ChallengeItem item : _ui.getChallengeItemList())
		{
			SolvableChallenge itemChallenge = getChallengeFor(item);
			if (itemChallenge == challenge)
			{
				_ui.select(item);
				return;
			}
		}
		throw new IllegalArgumentException("Challenge " + challenge
				+ " does not have an Item in the UI.");
	}

	/**
	 * Helper method to get the Challenge that corresponds to an Item. The
	 * ItemController holds the Challenge reference.
	 * 
	 * @param item
	 *            The item for which the challenge should be returned
	 * @return The SolvableChallenge, or null if none could be found
	 */
	private SolvableChallenge getChallengeFor(ChallengeItem item)
	{
		// XXX this could possibly refactored. maybe the SolvableChallenge
		// belongs inside the Item?
		if (item == null)
			return null;
		ChallengeItemController controller = _itemControllers.get(item);
		if (controller != null)
			return controller.getChallenge();
		else
			return null;
	}

	@Override
	public ReadOnlyObjectProperty<SolvableChallenge> challengeProperty()
	{
		return _selectedChObjectProperty;
	}

	private void setEditmode(boolean edit)
	{
		_editmodeProperty.set(edit);
	}

	@Override
	public ReadOnlyBooleanProperty editableProperty()
	{
		return _editmodeProperty;
	}



	@Ensures("result != null")
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
