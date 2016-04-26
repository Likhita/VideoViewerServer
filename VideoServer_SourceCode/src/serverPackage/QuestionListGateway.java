package serverPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class QuestionListGateway {

	ArrayList<ArrayList<String>> questionList=new ArrayList<ArrayList<String>>();
	int insertResult,updateResult;
	Connection connection;
	ResultSet resultset;
	
	QuestionListGateway(Integer videoId) throws SQLException, IOException{
		populateArrayList(findAll(videoId));
	}
	
	QuestionListGateway(Integer videoId,Integer afterQuestionId) throws SQLException, IOException{
		populateArrayList(findAfter(videoId,afterQuestionId));
	}
	
	QuestionListGateway(Integer videoId,String questionText,Integer videoTime) throws SQLException {
		insert(videoId,questionText,videoTime);
	}
	
	QuestionListGateway(String questionId) throws SQLException{
		updateNoOfAnswers(questionId);
	}
	
	void populateArrayList(ResultSet resultSet) throws SQLException{
		while(resultSet.next()){
			ArrayList<String> questionRow=new ArrayList<String>();
			questionRow.add(resultSet.getInt("question_id")+"");
			questionRow.add(escapeSpecialCharacter(resultSet.getString("question_text")));
			questionRow.add(resultSet.getInt("video_time")+"");
			questionRow.add(resultSet.getString("time_stamp"));
			questionRow.add(resultSet.getInt("answers")+"");
			questionList.add(questionRow);
		}
	}
	
	ResultSet findAll(Integer videoId) throws SQLException, IOException {
		String select = "SELECT question_id,question_text,video_time,time_stamp,answers FROM questionlist WHERE video_id= ?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, videoId);
		ResultSet resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}

 	ResultSet findAfter(Integer videoId,Integer afterQuestionId) throws SQLException, IOException {
 		String select= "SELECT question_id,question_text,video_time,time_stamp,answers FROM questionlist WHERE video_id= ? AND question_id> ?";
 		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, videoId);
		selectStatement.setObject( 2, afterQuestionId);
		resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
 	}
	
	void insert(Integer videoId,String questionText,Integer videoTime) throws SQLException {
		String insert = "INSERT INTO questionlist (video_id,question_text,video_time,time_stamp,answers) VALUES (?,?,?,?,?)";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement insertStatement = connection.prepareStatement(insert);	  
		insertStatement.setInt(1,videoId);
		insertStatement.setString(2,questionText);
		insertStatement.setInt(3,videoTime);
		insertStatement.setLong(4,new Date().getTime());
		insertStatement.setInt(5,0);
		insertResult=insertStatement.executeUpdate();
		DatabaseConnector.instance().closeConnection(connection);
	}
	
	void updateNoOfAnswers(String questionId) throws SQLException{
		String update = "UPDATE questionlist SET answers=answers+1 WHERE question_id=?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement updateStatement = connection.prepareStatement(update);	  
		updateStatement.setInt(1,Integer.valueOf(questionId));
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
	
	ArrayList<ArrayList<String>> getQuestionArray(){
		return questionList;
	}
	
	int isInsertSuccess(){
		return insertResult;
	}
	
	int isUpdateSuccess(){
		return updateResult;
	}
}
