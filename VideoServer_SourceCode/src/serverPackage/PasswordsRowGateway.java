package serverPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordsRowGateway {
	
	private String passwordHash;
	private String salt;
	
	PasswordsRowGateway(int aUser,String password,String aSalt){
		passwordHash=password;
		salt=aSalt;
	}
	
	public static PasswordsRowGateway find(Integer userID) throws SQLException, IOException {
		String select = "SELECT * FROM passwords WHERE username = ?";
		Connection connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, userID);
		ResultSet resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return load(resultset);	 
	}
	
	private static PasswordsRowGateway load(ResultSet aData) throws SQLException {
		aData.next();
		int userId = aData.getInt(1);
		String passwordHash = aData.getString(2);
		String salt = aData.getString(3);
		return new PasswordsRowGateway(userId,passwordHash,salt);
	}
	
	public String getSalt() {
		return salt; 
	}
	
	public String getPasswordHash() {
		return passwordHash; 
	}
}
