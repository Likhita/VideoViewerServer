package serverPackage;

import java.io.IOException;
import java.sql.SQLException;

import sdsu.util.ProgramProperties;

public class ServerDriver {

	public static void main(String args[]) throws IOException, SQLException {
		ProgramProperties flags = new ProgramProperties(args,"configurationFile");
		int port = flags.getInt( "port" , 10010);
		int time = flags.getInt( "timeout" , 600000);
		(new Server(port,time)).start();
    }
}
