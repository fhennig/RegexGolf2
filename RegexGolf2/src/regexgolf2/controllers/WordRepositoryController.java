package regexgolf2.controllers;

import java.io.IOException;

import javafx.scene.Parent;
import regexgolf2.services.repositories.WordRepository;
import regexgolf2.ui.wordrepository.WordRepositoryUI;

public class WordRepositoryController
{
	private final WordRepositoryUI _ui;
	private final WordRepository _repository;
	
	
	
	public WordRepositoryController(WordRepository repository) throws IOException
	{
		_ui = new WordRepositoryUI();
		_repository = repository;
		
		_repository.getAll(); //XXX only to stop compilation warnings
	}
	
	
	
	public Parent getUINode()
	{
		return _ui.getUINode();
	}
}
