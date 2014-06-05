package regexgolf2.services.propertyservice;

import com.google.java.contract.Ensures;

public interface Settings
{
	@Ensures("result != null")
	String getSQLiteDBPath();
}
