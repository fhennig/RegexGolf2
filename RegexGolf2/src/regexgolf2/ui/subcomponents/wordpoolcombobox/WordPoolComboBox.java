package regexgolf2.ui.subcomponents.wordpoolcombobox;

import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import regexgolf2.model.containers.Container;
import regexgolf2.model.containers.ContainerChangedEvent;
import regexgolf2.model.containers.ContainerChangedListener;
import regexgolf2.model.containers.WordPool;

public class WordPoolComboBox
{
	private final ComboBox<WordPoolItem> _comboBox = new ComboBox<>();
	private Optional<Container<WordPool>> _pools = Optional.empty();
	private final ContainerChangedListener<WordPool> _listener = e -> containerChanged(e);
	private final ObjectProperty<Optional<WordPool>> _selectedPool = new SimpleObjectProperty<>();



	public WordPoolComboBox()
	{
		_comboBox
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(o, oV, nV) -> {
							if (nV == null)
								setSelectedPool(null);
							else
								setSelectedPool(nV.getWordPool());
						});
	}


	private void containerChanged(ContainerChangedEvent<? extends WordPool> e)
	{
		e.getAddedItem().ifPresent(pool -> addItemFor(pool));
		e.getRemovedItem().ifPresent(pool -> removeItemFor(pool));
	}

	private void addItemFor(WordPool pool)
	{
		_comboBox.getItems().add(new WordPoolItem(pool));
	}

	private void removeItemFor(WordPool pool)
	{
		_comboBox.getItems().stream()
				.filter(item -> item.getWordPool() == pool)
				.findFirst()
				.ifPresent(item -> _comboBox.getItems().remove(item));
	}

	public void setItemSource(Container<WordPool> container)
	{
		_pools.ifPresent(pools -> pools.removeListener(_listener));
		_comboBox.getItems().clear();
		_pools = Optional.ofNullable(container);
		if (_pools.isPresent())
		{
			_pools.get().addListener(_listener);
			_pools.get().forEach(pool -> addItemFor(pool));
			_comboBox.getSelectionModel().selectFirst();
		}
	}

	public Optional<WordPool> getSelectedPool()
	{
		return _selectedPool.get();
	}

	private void setSelectedPool(WordPool pool)
	{
		_selectedPool.set(Optional.ofNullable(pool));
	}

	public ReadOnlyObjectProperty<Optional<WordPool>> selectedPoolProperty()
	{
		return _selectedPool;
	}

	public Node getUINode()
	{
		return _comboBox;
	}
}
