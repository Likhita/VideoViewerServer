package serverPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sdsu.util.ProgramProperties;

public class DatabaseConnector {

	String dbUrl,user,password;
	private static DatabaseConnector instance;
	
	private DatabaseConnector(String fileName) throws IOException, SQLException {
		ProgramProperties flags = new ProgramProperties(fileName);
		dbUrl = flags.getString( "dburl" , "");
		user = flags.getString( "user" , "");
		password = flags.getString( "password" , "");
	}
	
	public static void initializeDbConnector() throws IOException, SQLException{
		instance= new DatabaseConnector("configurationFile");
	}
	
	public static DatabaseConnector instance() {
		  return instance;
	}
	
	public ResultSet executeQuery( String sql ) throws SQLException {
		  return getStatement().executeQuery( sql);
	}
	
	public Statement getStatement() throws SQLException {
		  return getConnection().createStatement();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		  return getConnection().prepareStatement(sql);
	}
	
	public Connection getConnection() throws SQLException { 
		return DriverManager.getConnection( dbUrl, user, password);
	}
	
	void closeConnection(Connection connection) throws SQLException{
		connection.close();
	}
	
}
