package serverPackage;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseListGateway {

	Connection connection;
	ResultSet resultset;
	ArrayList<ArrayList<String>> courseList=new ArrayList<ArrayList<String>>();
	
	CourseListGateway() throws SQLException, IOException{
		populateArrayList(selectAll());
	}
	
	void populateArrayList(ResultSet resultSet) throws SQLException{
		while(resultSet.next()){
			ArrayList<String> courseRow=new ArrayList<String>();
			courseRow.add(resultSet.getString(1));
			courseRow.add(resultSet.getString(2));
			courseList.add(courseRow);
		}
	}
	
	ResultSet selectAll() throws SQLException, IOException {
		String select = "SELECT * FROM courselist";
		connection=DatabaseConnector.instance().getConnection();
		Statement selectStatement = connection.createStatement();
		resultset=selectStatement.executeQuery(select);
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}
	
	ArrayList<ArrayList<String>> getCourseArray(){
		return courseList;
	}
}
