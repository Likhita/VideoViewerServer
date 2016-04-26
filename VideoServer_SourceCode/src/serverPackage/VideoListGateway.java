package serverPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VideoListGateway {

	ArrayList<ArrayList<String>> videoList=new ArrayList<ArrayList<String>>();
	int updateResult;
	Connection connection;
	ResultSet resultset;
	
	VideoListGateway(Integer courseId) throws SQLException, IOException{
		populateArrayList(findAll(courseId));
	}
	
	VideoListGateway(Integer courseId,Integer afterVideoId) throws SQLException, IOException{
		populateArrayList(findAfter(courseId,afterVideoId));
	}
	
	VideoListGateway(String videoId) throws SQLException {
		updateNoOfQuestions(videoId);
	}
	
	void populateArrayList(ResultSet resultSet) throws SQLException{
		while(resultSet.next()){
			ArrayList<String> videoRow=new ArrayList<String>();
			videoRow.add(resultSet.getString("video_name"));
			videoRow.add(resultSet.getInt("video_id")+"");
			videoRow.add(resultSet.getDate("video_date").getTime()+"");
			videoRow.add(escapeSpecialCharacter(prependUrl(resultSet.getString("url"))));
			videoRow.add(resultSet.getInt("questions")+"");
			videoList.add(videoRow);
		}
	}
	
	ResultSet findAll(Integer courseId) throws SQLException, IOException {
		String select = "SELECT video_name,video_id,video_date,url,questions FROM videolist WHERE course_id= ?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, courseId);
		resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}
	
	ResultSet findAfter(Integer courseId,Integer afterVideoId) throws SQLException, IOException {
		String select = "SELECT video_name,video_id,video_date,url,questions FROM videolist WHERE course_id= ? AND video_id> ?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, courseId);
		selectStatement.setObject( 2, afterVideoId);
		resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}
	
	void updateNoOfQuestions(String videoId) throws SQLException{
		String update = "UPDATE videolist SET questions=questions+1 WHERE video_id=?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement updateStatement = connection.prepareStatement(update);	  
		updateStatement.setInt(1,Integer.valueOf(videoId));
		updateResult=updateStatement.executeUpdate();
		DatabaseConnector.instance().closeConnection(connection);
	}
	
	String escapeSpecialCharacter(String aString){
    	String finalString ="";
    	String replacedString;
    	for(int i=0;i<aString.length();i++){
    		char c=aString.charAt(i);
    		replacedString = checkString(c);
    		finalString = finalString + replacedString;    		
    	}
    	return finalString;
	}
	
	String checkString(char c){
		String newString = "";
		if (c == ';' || c == '\\' || c==':')
			newString = "\\"+c;
		else    
			newString = ""+c;
		return newString;
	}
	
	String prependUrl(String urlPart){
		return "http://www-rohan.sdsu.edu/~whitney/audio/courses/fall12"+urlPart;
	}
	
	ArrayList<ArrayList<String>> getVideoArray(){
		return videoList;
	}
	
	int isUpdateSuccess(){
		return updateResult;
	}
}
