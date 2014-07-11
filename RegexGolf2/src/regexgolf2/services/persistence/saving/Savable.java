package regexgolf2.services.persistence.saving;

import regexgolf2.services.persistence.PersistenceException;

/**
 * Interface that every class needs to implement, if it can be persisted.
 * This Interface is part of a Visitor pattern, the {@link #accept(SaveVisitor)} method be implemented like
 * {@code visitor.visit(this);}. This will lead to a compilation error in the {@link SaveVisitor} interface,
 * where the {@code visit} method needs to be added.
 */
public interface Savable
{
	void accept(SaveVisitor visitor) throws PersistenceException;
}
