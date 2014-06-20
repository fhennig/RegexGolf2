package regexgolf2.controllers;

import java.io.IOException;

import regexgolf2.ui.wordrepository.WordRepositoryUI;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	
	
	
	public WordRepositoryController() throws IOException
	{
		_ui = new WordRepositoryUI();
	}
	
	
	
	
	//TODO implement properly
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
