package serverPackage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ProcessRequest {
	private Socket socket;
	private UpToReader in;
	private Writer out;
	boolean logoutRequested=false;
	
	ProcessRequest(Socket aSocket) throws UnsupportedEncodingException, IOException, NoSuchAlgorithmException, SQLException{
		socket=aSocket;
		in=new UpToReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
		out=new OutputStreamWriter(socket.getOutputStream(), "UTF8");
		processRequestsFromClient();
	}
	
	void processRequestsFromClient() throws IOException, NoSuchAlgorithmException, SQLException{
		while(!logoutRequested){
			ProcessCommand processor=new ProcessCommand(readClientRequest(),socket);
			sendResult(processor.getResult()); 
			logoutRequested=processor.isLogoutRequested();
		}
		closeSocket();
		System.out.println("Socket closed");
	}
	
	void sendResult(String message) throws IOException {
		System.out.println("Response :");
		System.out.println(" result:"+message);
		out.append(message);
		out.flush();
	 }
		 
	 public String readClientRequest() throws IOException {
		 return in.upTo(";;");
	 }
	 
	 void closeSocket() throws IOException{
		 socket.close();
	 }
}
