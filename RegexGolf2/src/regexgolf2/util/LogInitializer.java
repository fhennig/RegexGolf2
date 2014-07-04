package regexgolf2.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogInitializer
{
	public static void initializeLoggingSettings()
	{
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.FINE);
		//Use Level INFO as a default.
		
		for (Handler h : rootLogger.getHandlers())
		{
			rootLogger.removeHandler(h);
		}
		
		rootLogger.addHandler(consoleHandler);
	}
}
