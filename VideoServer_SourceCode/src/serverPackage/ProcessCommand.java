package serverPackage;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ProcessCommand {
	Command aCommand;
	String request;
	String result;
	Socket socket;
	static boolean loginSuccessful;
	static boolean logoutRequested=false;
	
	public ProcessCommand(String aRequest,Socket aSocket) throws NoSuchAlgorithmException, SQLException, IOException{
		request=aRequest;
		socket=aSocket;
		handleCommands(getCommand(aRequest));
		ServerLogger.logger.info("IP address["+socket.getInetAddress()+"] requests ["+getCommand(aRequest)+"]");
	}

	String getCommand(String request){
		int startIndex=request.indexOf(';');
		String command=request.substring(0, startIndex);
		return command;
	}
	
	void handleCommands(String command) throws NoSuchAlgorithmException, SQLException, IOException{
		int count=0;
		if(command.equalsIgnoreCase("Login")){
			aCommand=new ProcessLoginCommand(request);
			loginSuccessful=aCommand.getLoginStatus();
			result=((ProcessLoginCommand) aCommand).getResult();  
		}else if(command.equalsIgnoreCase("courselist") && loginSuccessful==true){
			aCommand=new ProcessCourseList(request);
			result=((ProcessCourseList) aCommand).getResult(); 
		}else if(command.equalsIgnoreCase("videolist") && loginSuccessful==true){
			aCommand=new ProcessVideoList(request);
			result=((ProcessVideoList) aCommand).getResult(); 
		}else if(command.equalsIgnoreCase("questionlist") && loginSuccessful==true){
			aCommand=new ProcessQuestionList(request);
			result=((ProcessQuestionList) aCommand).getResult(); 
		}else if(command.equalsIgnoreCase("answerlist") && loginSuccessful==true){
			aCommand=new ProcessAnswerList(request);
			result=((ProcessAnswerList) aCommand).getResult(); 
		}else if(command.equalsIgnoreCase("questionadd") && loginSuccessful==true){
			aCommand=new ProcessQuestionAdd(request);
			result=((ProcessQuestionAdd) aCommand).getResult(); 
		}else if(command.equalsIgnoreCase("answeradd") && loginSuccessful==true){
			aCommand=new ProcessAnswerAdd(request);
			result=((ProcessAnswerAdd) aCommand).getResult(); 
		}
		else if(loginSuccessful==false){
			result="error: invalid command, please login;;";
		}else if(command.equalsIgnoreCase("logout")){
			logoutRequested=true;
			result="ok:success;;"; 
		}
		else{
			if(count>3){
				result="Too many errors, closing connection !";
				socket.close();
			}else{
				result="error: invalid command;;";
			}
		count++;
		}
	}
	
	String getResult(){
		return result;
	}
	
	boolean isLogoutRequested(){
		return logoutRequested;
	}
}
