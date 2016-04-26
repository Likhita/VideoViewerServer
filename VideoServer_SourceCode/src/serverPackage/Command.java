package serverPackage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Command {

	String result;
	boolean loginSuccessful=false;
	
	void setLoginSuccessful(){
		loginSuccessful=true;
	}
	
	boolean getLoginStatus(){
		return loginSuccessful;
	}
	
	String getResult(){
		return result;
	}
}

class ProcessLoginCommand extends Command{
	
	Integer userId;
	String password;
	static final String HEXES = "0123456789ABCDEF";
	
	ProcessLoginCommand(String request) throws NoSuchAlgorithmException, SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		checkWithDatabase(); 
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("Id")){
			userId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}
		else
			result="error:Invalid username;;";
		
		if(request.substring(0, colonIndex).equalsIgnoreCase("password")){
			password=request.substring(colonIndex+1, endIndex);
		}
		else
			result="error:Invalid password;;";
		
	}
	
	void checkWithDatabase() throws SQLException, NoSuchAlgorithmException, IOException{
		PasswordsRowGateway row=PasswordsRowGateway.find(userId); 
		if(computeHash(row.getSalt()).equals(row.getPasswordHash())){
			result="ok:success;;";
			setLoginSuccessful();
		}
		else
			result="error:login invalid;;";
	}
	
	private String computeHash(String aSalt) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update((password+aSalt).getBytes());
		byte[] md5Hash = md5.digest();
		String hex=getHex(md5Hash);
		return hex;
	}
	
	private String getHex(byte [] hash){
		if(hash==null)
			return null;
		StringBuilder hex =new StringBuilder(2 * hash.length);
		for(final byte b : hash){
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}

class ProcessCourseList extends Command{
	
	
	ProcessCourseList(String request) throws SQLException, IOException{
		System.out.println("Request: "+request);
		getDataFromDatabase();
	}
	
	void getDataFromDatabase() throws SQLException, IOException{
		CourseListGateway database =new CourseListGateway(); 
		createResponse(database.getCourseArray());
	}
	
	void createResponse(ArrayList<ArrayList<String>> courseList){
		result="ok:"+courseList.size()+";";
		for(int i=0;i<courseList.size();i++){
			result=result+"name:"+courseList.get(i).get(1)+";id:"+courseList.get(i).get(0)+";"+'\r';
		}
		int lastCr=result.lastIndexOf('\r');
		result=result.substring(0, lastCr);
		result=result+";";
	}
}

class ProcessVideoList extends Command{
	
	int courseId;
	int afterVideoId=0;
	
	ProcessVideoList(String request) throws SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		if(afterVideoId==0)
			getDataFromDatabase();
		else
			getDataAfterFromDatabase();
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("course")){
			courseId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}if(request.indexOf(':')!=-1){
			if(request.substring(0, colonIndex).equalsIgnoreCase("after")){
				afterVideoId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
				request=request.substring(endIndex+1);
				endIndex=request.indexOf(';');
				colonIndex=request.indexOf(':');
			}
		}else
			result="error:Invalid command;;";
	}
	
	void getDataFromDatabase() throws SQLException, IOException{
		VideoListGateway database =new VideoListGateway(courseId); 
		createResponse(database.getVideoArray());
	}
	
	void getDataAfterFromDatabase() throws SQLException, IOException{
		VideoListGateway database =new VideoListGateway(courseId,afterVideoId); 
		createResponse(database.getVideoArray());
	}
	
	void createResponse(ArrayList<ArrayList<String>> videoList){
		if(videoList.size()==0){
			result="error: course not found;;";
		}
		else {
			result="ok:"+videoList.size()+";";
			for(int i=0;i<videoList.size();i++){
				result=result+"name:"+videoList.get(i).get(0)+";id:"+videoList.get(i).get(1)+";date:"+videoList.get(i).get(2)+";url:"+videoList.get(i).get(3)+";questions:"+videoList.get(i).get(4)+";"+'\r';
			}
			int lastCr=result.lastIndexOf('\r');
			result=result.substring(0, lastCr);
			result=result+";";
		}
	}
}

class ProcessQuestionList extends Command{
	
	int videoId;
	int afterQuestionId=0;
	
	ProcessQuestionList(String request) throws SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		if(afterQuestionId==0)
			getDataFromDatabase();
		else
			getDataAfterFromDatabase();
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("video")){
			videoId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}if(request.indexOf(':')!=-1){
			if(request.substring(0, colonIndex).equalsIgnoreCase("after")){
				afterQuestionId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
				request=request.substring(endIndex+1);
				endIndex=request.indexOf(';');
				colonIndex=request.indexOf(':');
			}
		}else
			result="error:Invalid command;;";
	}
	
	void getDataFromDatabase() throws SQLException, IOException{
		QuestionListGateway database =new QuestionListGateway(videoId); 
		createResponse(database.getQuestionArray());
	}
	
	void getDataAfterFromDatabase() throws SQLException, IOException{
		QuestionListGateway database =new QuestionListGateway(videoId,afterQuestionId); 
		createResponse(database.getQuestionArray());
	}
	
	void createResponse(ArrayList<ArrayList<String>> questionList){
		if(questionList.size()==0){
			result="error: video not found;;";
		}
		else {
			result="ok:"+questionList.size()+";";
			for(int i=0;i<questionList.size();i++){
				result=result+"id:"+questionList.get(i).get(0)+";text:"+questionList.get(i).get(1)+";time:"+questionList.get(i).get(2)+";timestamp:"+questionList.get(i).get(3)+";answers:"+questionList.get(i).get(4)+";"+'\r';
			}
			int lastCr=result.lastIndexOf('\r');
			result=result.substring(0, lastCr);
			result=result+";";
		}
	}
}

class ProcessAnswerList extends Command{
	
	int questionId;
	int afterAnswerId=0;
	
	ProcessAnswerList(String request) throws SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		if(afterAnswerId==0)
			getDataFromDatabase();
		else
			getDataAfterFromDatabase();
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("question")){
			questionId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}if(request.indexOf(':')!=-1){
			if(request.substring(0, colonIndex).equalsIgnoreCase("after")){
				afterAnswerId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
				request=request.substring(endIndex+1);
				endIndex=request.indexOf(';');
				colonIndex=request.indexOf(':');
			}
		}else
			result="error:Invalid command;;";
	}
	
	void getDataFromDatabase() throws SQLException, IOException{
		AnswerListGateway database =new AnswerListGateway(questionId); 
		createResponse(database.getAnswerArray());
	}
	
	void getDataAfterFromDatabase() throws SQLException, IOException{
		AnswerListGateway database =new AnswerListGateway(questionId,afterAnswerId); 
		createResponse(database.getAnswerArray());
	}
	
	void createResponse(ArrayList<ArrayList<String>> answerList){
		if(answerList.size()==0){
			result="error: question not found;;";
		}
		else {
			result="ok:"+answerList.size()+";";
			for(int i=0;i<answerList.size();i++){
				result=result+"id:"+answerList.get(i).get(0)+";text:"+answerList.get(i).get(1)+";timestamp:"+answerList.get(i).get(2)+";"+'\r';
			}
			int lastCr=result.lastIndexOf('\r');
			result=result.substring(0, lastCr);
			result=result+";";
		}
	}
}

class ProcessQuestionAdd extends Command{
	
	Integer videoId;
	String questionText;
	Integer videoTime;
	
	ProcessQuestionAdd(String request) throws NoSuchAlgorithmException, SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		updateDatabase(); 
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("video")){
			videoId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');

		}
		if(request.substring(0, colonIndex).equalsIgnoreCase("text")){
			questionText=unescapeSpecialCharacter(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}
		if(request.substring(0, colonIndex).equalsIgnoreCase("time")){
			videoTime=Integer.valueOf(request.substring(colonIndex+1, endIndex));
		}
		else
			result="error:Invalid questinAdd format;;";
	}
	
	void updateDatabase() throws SQLException, NoSuchAlgorithmException, IOException{
		QuestionListGateway questionList=new QuestionListGateway(videoId,questionText,videoTime); 
		VideoListGateway videoList=new VideoListGateway(videoId.toString());
		if(questionList.isInsertSuccess()==1 && videoList.isUpdateSuccess()==1)
			result="ok:success;;";
		else
			result="error:Database error;;";
	}
	
	String unescapeSpecialCharacter(String aString){
    	if(aString.contains("\\;"))
			aString=aString.replace("\\;", ";");
		else if(aString.contains("\\:"))
			aString=aString.replace("\\:", ":");
		else if(aString.contains("\\"))
			aString=aString.replace("\\", "");
		return aString;
    }
}

class ProcessAnswerAdd extends Command{
	
	Integer questionId;
	String answerText;
	
	ProcessAnswerAdd(String request) throws NoSuchAlgorithmException, SQLException, IOException{
		System.out.println("Request: "+request);
		getParameters(request);
		updateDatabase(); 
	}
	
	void getParameters(String request){
		int startIndex=request.indexOf(';');
		request=request.substring(startIndex+1);
		int endIndex=request.indexOf(';');
		int colonIndex=request.indexOf(':');
		if(request.substring(0, colonIndex).equalsIgnoreCase("question")){
			questionId=Integer.valueOf(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}
		if(request.substring(0, colonIndex).equalsIgnoreCase("text")){
			answerText=unescapeSpecialCharacter(request.substring(colonIndex+1, endIndex));
			request=request.substring(endIndex+1);
			endIndex=request.indexOf(';');
			colonIndex=request.indexOf(':');
		}
		else
			result="error:Invalid answerAdd format;;";
	}
	
	void updateDatabase() throws SQLException, NoSuchAlgorithmException, IOException{
		AnswerListGateway answerList=new AnswerListGateway(questionId,answerText);
		QuestionListGateway questionList=new QuestionListGateway(questionId.toString()); 
		if(answerList.isInsertSuccess()==1 && questionList.isUpdateSuccess()==1)
			result="ok:success;;";
		else
			result="error:failure;;";
	}
	
	String unescapeSpecialCharacter(String aString){
    	if(aString.contains("\\;"))
			aString=aString.replace("\\;", ";");
		else if(aString.contains("\\:"))
			aString=aString.replace("\\:", ":");
		else if(aString.contains("\\"))
			aString=aString.replace("\\", "");
		return aString;
    }
	
}