package com.notifcation.service.main;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;

import com.notification.service.Service;
import com.notification.service.model.SMSEmailQueue;

public class SMSEmailQueueServiceImpl {

	private static SMSEmailQueueRepository smsEmailRepo = new SMSEmailQueueRepository();
	private static String debugLabel = "SMSEmailQueueThread";
	
	public SMSEmailQueueServiceImpl(){}
	
	public List<SMSEmailQueue> getSMSEmailQueue(){
		return smsEmailRepo.getSMSEmailQueue();
	}
	
	public int updateStatusSend( long id ){
		return smsEmailRepo.updateStatusSend(id);
	}
	
	public int updateStatusResponse( long id, int statusResponse, String responseMessage ){
		return smsEmailRepo.updateStatusResponse(id, statusResponse, responseMessage);
	}
	
	public JSONObject sendEmail( String email,
								 String subject,
								 String body,
								 String hostMail,
								 int portMail,
								 String usernameMail,
								 String passwordMail){
		
		JSONObject joResult 	= new JSONObject();
		
		Properties props 		= new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
  		props.put("mail.smtp.host", hostMail);
  		props.put("mail.smtp.port", portMail);
  		
  		Session session = Session.getInstance(props,
   			new javax.mail.Authenticator() {
   			protected PasswordAuthentication getPasswordAuthentication() {
   				return new PasswordAuthentication(usernameMail, passwordMail);
   			}
   		 });
  		
  		session.setDebug(true);
  		
  		try{
  			
  			MimeMessage message = new MimeMessage(session);
   			message.setFrom(new InternetAddress(usernameMail));
   			
   			if( !email.equals("") && email != null && !email.equals("''") && !email.equals("null") ){
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				message.setSubject(subject);
				message.setContent(body, "text/html");
				
				Transport transport = session.getTransport("smtp");
				transport.connect (hostMail, portMail, usernameMail, passwordMail);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();    			
				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "Send email successfully");
   			}else{
   				
   				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Can't send email. Email destination not valid.");
   				
   			}
  			
  		}catch (MessagingException e) {
  			Service.writeDebug("Exception<sendEmail> : " + e.getMessage(), debugLabel);
  			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Exception : " + e.getMessage());
  			throw new RuntimeException(e);
 		}
		
		return joResult;
		
	}
	
}
