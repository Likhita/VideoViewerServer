package serverPackage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {

	static Logger logger = Logger.getLogger("edu.serverPackage.ServerLogger");
	 
	static {
		try {
			Handler accessLog = new FileHandler("ServerLog.txt",true);
			accessLog.setFormatter( new SimpleFormatter());
			accessLog.setLevel(Level.ALL);
			logger.addHandler(accessLog);
			logger.setLevel(Level.ALL);
	    	}
		catch (IOException fileError) {
			System.err.println( "Could not open log files");
	    }
	}
}
