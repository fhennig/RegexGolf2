package regexgolf2.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import regexgolf2.model.SolvableChallenge;
import regexgolf2.services.ServiceChangedListener;
import regexgolf2.services.challengerepository.ChallengeRepository;
import regexgolf2.ui.challengerepositoryview.ChallengeRepositoryUI;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeItem;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class ChallengeRepositoryController
{
	private final ChallengeRepository _challengeRepo;
	private final ChallengeRepositoryUI _ui;
	private final Map<ChallengeItem, ChallengeItemController> _itemControllers = new HashMap<>();
	private final ObjectProperty<SolvableChallenge> _selectedChObjectProperty = new SimpleObjectProperty<>();
	private final BooleanProperty _editmodeProperty = new SimpleBooleanProperty();
	
	
	
	@Requires("challengeRepo != null")
	public ChallengeRepositoryController(ChallengeRepository challengeRepo) throws IOException
	{
		_challengeRepo = challengeRepo;
		_ui = new ChallengeRepositoryUI();

		initButtonBindings();
		initListeners();
		initAddButtonHandler();
		initRemoveButtonHandler();
		initSaveButtonHandler();
		refreshListViewItemList();
	}
	
	
	
	private void initButtonBindings()
	{
		_ui.getRemoveButton().disableProperty().bind(_selectedChObjectProperty.isNull());
		_ui.getEditButton().disableProperty().bind(_selectedChObjectProperty.isNull());
		
		_ui.getEditButton().selectedProperty().bindBidirectional(_editmodeProperty);
	}
	
	private void initListeners()
	{
		_ui.selectedChallengeProperty().addListener(new ChangeListener<ChallengeItem>()
		{
			@Override
			public void changed(ObservableValue<? extends ChallengeItem> observable,
					ChallengeItem oldValue, ChallengeItem newValue)
			{
				if (newValue != null)
					//Get the Challenge that corresponds to the Item
					_selectedChObjectProperty.set(_itemControllers.get(newValue).getChallenge());
				else
					_selectedChObjectProperty.set(null);
				
				//If the selected challenge changed, the edit mode is resetted to false
				setEditmode(false);
			}
			
		});
		
		_challengeRepo.addServiceChangedListener(new ServiceChangedListener()
		{
			@Override
			public void serviceChanged(EventObject event)
			{
				refreshListViewItemList();
			}
		});
	}
	
	private void initAddButtonHandler()
	{
		_ui.getAddButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				SolvableChallenge newChallenge = _challengeRepo.createNew();
				selectChallenge(newChallenge);
				setEditmode(true);
			}
		});
	}
	
	private void initRemoveButtonHandler()
	{
		_ui.getRemoveButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				ChallengeItem item = _ui.getSelectedChallengeItem();
				assert item != null : "Remove Button was clicked by no challenge was selected.";
				
				SolvableChallenge challenge = _itemControllers.get(item).getChallenge();
				assert _challengeRepo.contains(challenge) : "The challenge to remove is not contained in the ChallengeRepo."; 
				try
				{
					_challengeRepo.delete(challenge);
				} catch (SQLException e)
				{
					//TODO use fancy dialog here
					JOptionPane.showMessageDialog(null, "Error while accessing the database!\n" +
												"Challenge was not deleted.");
				}
			}
		});
	}
	
	private void initSaveButtonHandler()
	{
		_ui.getSaveButton().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				try
				{
					_challengeRepo.saveAll();
				} catch (SQLException e)
				{
					//TODO use fancy error dialog here
					JOptionPane.showMessageDialog(null, "Error in DB");
				}
			}
		});
	}
	
	/**
	 * Synchronizes the List of Challenges in the UI with the List of Challenges
	 * in the Repository.
	 * This method ensures, that for every Challenge in the Repository, there exists an
	 * item in the UI.
	 * This method is called from the ServiceChangedListener that listens to the Repository.
	 */
	private void refreshListViewItemList()
	{
		List<ChallengeItem> toRemove = new ArrayList<>();
		for (ChallengeItem item : _ui.getChallengeItemList())
		{
			if (!_challengeRepo.contains(_itemControllers.get(item).getChallenge()))
				toRemove.add(item);
		}
		for (ChallengeItem item : toRemove)
			removeItem(item);
		
		for (SolvableChallenge c : _challengeRepo.getAll())
		{
			boolean itemExists = false;
			for (ChallengeItem item : _ui.getChallengeItemList())
			{
				if (_itemControllers.get(item).getChallenge() == c)
				{
					itemExists = true;
					break;
				}
			}
			if (!itemExists)
				addItem(c);
		}
	}
	
	private void removeItem(ChallengeItem item)
	{
		_ui.getChallengeItemList().remove(item);
		_itemControllers.get(item).discard();
		_itemControllers.remove(item);
	}
	
	/**
	 * Initializes a new Controller with the given Challenge and
	 * puts the Controller in the map.
	 * The Item controlled by the Controller is added to the UI list.
	 * @return  The item that was added to the UI.
	 */
	private ChallengeItem addItem(SolvableChallenge challenge)
	{
		ChallengeItemController controller = new ChallengeItemController(challenge, _challengeRepo.getPersistenceState(challenge));
		_ui.getChallengeItemList().add(controller.getItem());
		_itemControllers.put(controller.getItem(), controller);
		return controller.getItem();
	}
	
	/**
	 * Selects the ChallengeItem in the UI for a given SolvableChallenge.
	 * This is used to immediately select a newly created Challenge.
	 * @param challenge  the 
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
		throw new IllegalArgumentException("Challenge " + challenge + " does not have an Item in the UI.");
	}
	
	/**
	 * Helper method to get the Challenge that corresponds to an Item.
	 * The ItemController holds the Challenge reference.
	 * @param item  The item for which the challenge should be returned 
	 * @return The SolvableChallenge, or null if none could be found
	 */
	private SolvableChallenge getChallengeFor(ChallengeItem item)
	{
		//XXX this could possibly refactored. maybe the SolvableChallenge belongs inside the Item?
		if (item == null)
			return null;
		ChallengeItemController controller = _itemControllers.get(item);
		if (controller != null)
			return controller.getChallenge();
		else
			return null;
	}

	/**
	 * Returns a Property that holds the currently selected Challenge.
	 * This can be null, if no Challenge is selected.
	 */
	@Ensures("result != null")
	public ReadOnlyObjectProperty<SolvableChallenge> selectedChallengeProperty()
	{
		return _selectedChObjectProperty;
	}
	
	private void setEditmode(boolean edit)
	{
		_editmodeProperty.set(edit);
	}
	
	@Ensures("result != null")
	public ReadOnlyBooleanProperty editModeProperty()
	{
		return _editmodeProperty;
	}
	
	@Ensures("result != null")
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
