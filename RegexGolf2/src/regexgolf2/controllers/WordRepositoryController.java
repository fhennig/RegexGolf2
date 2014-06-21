package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import regexgolf2.ui.wordrepository.WordRepositoryUI;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	
	
	
	public WordRepositoryController() throws IOException
	{
		_ui = new WordRepositoryUI();
	}
	
	
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
