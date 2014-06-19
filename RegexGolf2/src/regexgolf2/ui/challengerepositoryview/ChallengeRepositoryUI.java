package regexgolf2.ui.challengerepositoryview;

import java.io.IOException;
import java.util.List;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import regexgolf2.ui.subcomponents.challengelisting.ChallengeListUI;
import regexgolf2.ui.subcomponents.challengelisting.challengecell.ChallengeItem;
import regexgolf2.ui.util.JavafxUtil;

public class ChallengeRepositoryUI
{
    @FXML
    private AnchorPane _challengeViewPane;

    @FXML
    private Button _removeButton;

    @FXML
    private Button _addButton;
    
    @FXML
    private ToggleButton _editButton;

    @FXML
    private Button _saveButton;
    
    private final Node _rootNode;
    
    private ChallengeListUI _challengeListUI = new ChallengeListUI();
    
    
    
    public ChallengeRepositoryUI() throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeRepositoryUI.fxml")); 
    	loader.setController(this);
    	_rootNode = loader.load();
    	
    	assert _challengeViewPane != null;
    	assert _removeButton != null;
    	assert _addButton != null;
    	assert _saveButton != null;
    	
    	JavafxUtil.setAsContent(_challengeListUI.getUINode(), _challengeViewPane);
    }

    
    
    public Button getRemoveButton()
    {
    	return _removeButton;
    }
    
    public Button getAddButton()
    {
    	return _addButton;
    }
    
    public ToggleButton getEditButton()
    {
    	return _editButton;
    }
    
    public Button getSaveButton()
    {
    	return _saveButton;
    }
    
    public void select(ChallengeItem item)
    {
    	_challengeListUI.select(item);
    }
    
    public ReadOnlyBooleanProperty challengeSelectedProperty()
    {
    	return _challengeListUI.challengeSelectedProperty();
    }
    
    public ChallengeItem getSelectedChallengeItem()
    {
    	return _challengeListUI.selectedChallengeProperty().get();
    }
    
    public ReadOnlyObjectProperty<ChallengeItem> selectedChallengeProperty()
    {
    	return _challengeListUI.selectedChallengeProperty();
    }
    
    public List<ChallengeItem> getChallengeItemList()
    {
    	return _challengeListUI.getChallengeItems();
    }
    
    public Node getUINode()
    {
    	return _rootNode;
    }
}