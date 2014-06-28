package regexgolf2.ui.challengegenerator;

import java.io.IOException;

import com.google.java.contract.Requires;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import regexgolf2.ui.util.JavafxUtil;

public class ChallengeGeneratorUI
{
	@FXML
	private ChoiceBox<?> _generatorChoiceBox;

	@FXML
	private Button _wordPoolButton;

	@FXML
	private Button _generateButton;

	@FXML
	private AnchorPane _generatorConfigPane;

	private final AnchorPane _wordRepositoryPane = new AnchorPane();
	private Stage _wordRepositoryStage;

	private final Window _parentWindow;

	private Node _rootNode;

	

	@Requires("parent != null")
	public ChallengeGeneratorUI(Window parent) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChallengeGeneratorUI.fxml"));
		loader.setController(this);
		_rootNode = loader.load();

		assert _generatorChoiceBox != null;
		assert _wordPoolButton != null;
		assert _generatorConfigPane != null;
		assert _generateButton != null;

		_parentWindow = parent;
		_wordPoolButton.setOnAction(e -> wordPoolButtonClicked());
	}



	private void wordPoolButtonClicked()
	{
		// lazy initialization
		if (_wordRepositoryStage == null)
			initWordRepoStage();
		_wordRepositoryStage.show();
		_wordRepositoryStage.requestFocus();
	}

	/**
	 * Called from the Button Handler because of lazy initialization
	 */
	private void initWordRepoStage()
	{
		_wordRepositoryStage = new Stage();
		_wordRepositoryStage.setScene(new Scene(_wordRepositoryPane));
		_wordRepositoryStage.initOwner(_parentWindow);
		// Set dimensions:
		_wordRepositoryStage.setWidth(400);
		_wordRepositoryStage.setHeight(580);
		// Center stage in parentstage
		JavafxUtil.centerInParent(_wordRepositoryStage, _parentWindow);
	}

	// TODO method for choicebox

	public Button getWordPoolButton()
	{
		return _wordPoolButton;
	}

	public Button getGenerateButton()
	{
		return _generateButton;
	}

	public void setGeneratorConfigPaneContent(Node content)
	{
		JavafxUtil.setAsContent(content, _generatorConfigPane);
	}

	public void setWordRepositoryPanel(Node content)
	{
		JavafxUtil.setAsContent(content, _wordRepositoryPane);
	}

	public Node getUINode()
	{
		return _rootNode;
	}
}
