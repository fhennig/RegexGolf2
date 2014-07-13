package regexgolf2.model;



public interface ObservableObject
{

	public abstract void addObjectChangedListener(ObjectChangedListener listener);

	public abstract void removeObjectChangedListener(ObjectChangedListener listener);

}