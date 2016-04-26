package serverPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class AnswerListGateway {

	ArrayList<ArrayList<String>> answerList=new ArrayList<ArrayList<String>>();
	int insertResult;
	Connection connection;
	ResultSet resultset;
	
	AnswerListGateway(Integer questionId) throws SQLException, IOException{
		populateArrayList(findAll(questionId));
	}
	
	AnswerListGateway(Integer questionId,Integer afterAnswerId) throws SQLException, IOException{
		populateArrayList(findAfter(questionId,afterAnswerId));
	}
	
	AnswerListGateway(Integer questionId,String answerText) throws SQLException {
		insert(questionId,answerText);
	}
	
	void populateArrayList(ResultSet resultSet) throws SQLException{
		while(resultSet.next()){
			ArrayList<String> answerRow=new ArrayList<String>();
			answerRow.add(resultSet.getInt("answer_id")+"");
			answerRow.add(escapeSpecialCharacter(resultSet.getString("answer_text")));
			answerRow.add(resultSet.getString("time_stamp"));
			answerList.add(answerRow);
		}
	}
	
	ResultSet findAll(Integer questionId) throws SQLException, IOException {
		String select = "SELECT answer_id,answer_text,time_stamp FROM answerlist WHERE question_id= ?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, questionId);
		resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}
	
	ResultSet findAfter(Integer questionId,Integer afterAnswerId) throws SQLException, IOException {
		String select = "SELECT answer_id,answer_text,time_stamp FROM answerlist WHERE question_id= ? AND answer_id> ?";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement selectStatement = connection.prepareStatement(select);
		selectStatement.setObject( 1, questionId);
		selectStatement.setObject( 1, afterAnswerId);
		resultset=selectStatement.executeQuery();
		DatabaseConnector.instance().closeConnection(connection);
		return resultset;
	}
	
	void insert(int questionId,String answerText) throws SQLException {
		String insert = "INSERT INTO answerlist (question_id,answer_text,time_stamp) VALUES (?,?,?)";
		connection=DatabaseConnector.instance().getConnection();
		PreparedStatement insertStatement = connection.prepareStatement(insert);	  
		insertStatement.setInt(1,questionId);
		insertStatement.setString(2,answerText);
		insertStatement.setLong(3,new Date().getTime());
		insertResult=insertStatement.executeUpdate();
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
	
	ArrayList<ArrayList<String>> getAnswerArray(){
		return answerList;
	}
	
	int isInsertSuccess(){
		return insertResult;
	}

}
