package regexgolf2.services.settingsservice;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public interface Settings
{
	@Ensures("result != null")
	String getSQLiteDBPath();
	
	@Requires("path != null")
	void setSQLiteDBPath(String path);
}
