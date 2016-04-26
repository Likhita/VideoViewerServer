package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{

	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	
	public Server(int port,int timeout) throws IOException, SQLException {
	     serverSocket = new ServerSocket(port);
	     serverSocket.setSoTimeout(timeout);
	     pool = Executors.newCachedThreadPool();
	     DatabaseConnector.initializeDbConnector();
	}
	
	public void run() {
	     try {
	    	 ServerLogger.logger.entering("MessageTypes", "Server Log Messages");
	    	 ServerLogger.logger.info("Server listening on port ["+serverSocket.getLocalPort()+"]");
	    	 ServerLogger.logger.info("Server Address ["+serverSocket.getLocalSocketAddress().toString()+"]");
	    	 for (;;) {
	    		 System.out.println(" server waiting to accept..");
	    		 pool.execute(new Handler(serverSocket.accept()));
	    	 }
	     } catch (IOException ex) {
	    	 pool.shutdown();
	     }
	}
}
