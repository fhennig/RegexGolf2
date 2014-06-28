package regexgolf2.ui.modules;

import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import regexgolf2.ui.util.JavafxUtil;

import com.google.java.contract.Ensures;

public class ModulesUI
{
	@FXML
	private AnchorPane _savedChallengesPane;

	@FXML
	private AnchorPane _challengeGeneratorPane;

	@FXML
	private Tab _savedChallengesTab;

	@FXML
	private Tab _challengeGeneratorTab;

	private final TabPane _rootNode;




	public ModulesUI() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ModulesUI.fxml"));
		loader.setController(this);
		_rootNode = loader.load();

		assert _savedChallengesPane != null;
		assert _challengeGeneratorPane != null;
	}

	public Tab getSavedChallengesTab()
	{
		return _savedChallengesTab;
	}

	public Tab getChallengeGeneratorTab()
	{
		return _challengeGeneratorTab;
	}

	public Tab getSelectedTab()
	{
		return _rootNode.getSelectionModel().getSelectedItem();
	}

	public ReadOnlyObjectProperty<Tab> selectedTabProperty()
	{
		return _rootNode.getSelectionModel().selectedItemProperty();
	}

	public void setChallengeRepoPanelContent(Node content)
	{
		JavafxUtil.setAsContent(content, _savedChallengesPane);
	}

	public void setChallengeGeneratorPanel(Node content)
	{
		JavafxUtil.setAsContent(content, _challengeGeneratorPane);
	}

	@Ensures("result != null")
	public Node getUINode()
	{
		return _rootNode;
	}
}
