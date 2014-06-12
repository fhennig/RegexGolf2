package regexgolf2.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
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

import com.google.java.contract.Requires;

public class ChallengeRepositoryController
{
	private final ChallengeRepository _challengeRepo;
	private final ChallengeRepositoryUI _ui;
	private final Map<ChallengeItem, ChallengeItemController> _controllers = new HashMap<>();
	private final ObjectProperty<SolvableChallenge> _selectedChObjectProperty = new SimpleObjectProperty<>();
	
	
	
	@Requires("challengeRepo != null")
	public ChallengeRepositoryController(ChallengeRepository challengeRepo) throws IOException
	{
		_challengeRepo = challengeRepo;
		_ui = new ChallengeRepositoryUI();

		initListeners();
		initAddButtonHandler();
		initRemoveButtonHandler();
		initSaveButtonHandler();
		refreshListViewItemList();
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
					_selectedChObjectProperty.set(_controllers.get(newValue).getChallenge());
				else
					_selectedChObjectProperty.set(null);
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
				
				SolvableChallenge challenge = _controllers.get(item).getChallenge();
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
	
	private void refreshListViewItemList()
	{
		List<ChallengeItem> toRemove = new ArrayList<>();
		for (ChallengeItem item : _ui.getChallengeItemList())
		{
			if (!_challengeRepo.contains(_controllers.get(item).getChallenge()))
				toRemove.add(item);
		}
		for (ChallengeItem item : toRemove)
			removeItem(item);
		
		for (SolvableChallenge c : _challengeRepo.getAll())
		{
			boolean itemExists = false;
			for (ChallengeItem item : _ui.getChallengeItemList())
			{
				if (_controllers.get(item).getChallenge() == c)
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
		_controllers.get(item).discard();
		_controllers.remove(item);
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
		_controllers.put(controller.getItem(), controller);
		return controller.getItem();
	}
	
	private void selectChallenge(SolvableChallenge challenge)
	{
		for (ChallengeItem item : _ui.getChallengeItemList())
		{
			SolvableChallenge itemChallenge = _controllers.get(item).getChallenge();
			if (itemChallenge == challenge)
			{
				_ui.select(item);
				return;
			}
		}
		throw new IllegalArgumentException("Challenge " + challenge + " does not have an Item in the UI.");
	}

	/**
	 * Can contain null!
	 */
	public ReadOnlyObjectProperty<SolvableChallenge> selectedChallengeProperty()
	{
		return _selectedChObjectProperty;
	}
	
	public Node getUINode()
	{
		return _ui.getUINode();
	}
}
