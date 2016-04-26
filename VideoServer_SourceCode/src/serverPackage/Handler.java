package serverPackage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

class Handler implements Runnable {
	   private final Socket socket;
	   
	   Handler(Socket socket) { 
	      this.socket = socket; 
	   }
	   
	   public void run() {
		   try {
			   System.out.println(" processing request..");  
			   new ProcessRequest(socket);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   }
}
