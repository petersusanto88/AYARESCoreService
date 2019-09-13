package com.notification.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notifcation.service.main.FCMNotificationService;
import com.notifcation.service.main.FCMNotificationServiceImpl;
import com.notification.service.model.FCMNotification;
import com.notification.service.util.JSON;

public class FCMNotificationThread extends Thread {

	private static FCMNotificationService fcmService 		= new FCMNotificationServiceImpl();
	private static ObjectMapper objMap						= new ObjectMapper();
	private static String debugLabel						= "FCMNotificationThread";
	
	public void run(){
		
		try{		
			
			if( !this.stop ){		
				
				Service.writeDebug("Starting FCMNotificationThread...",debugLabel);
				
				while( true ){				
					
					List<FCMNotification> lNotif 			= fcmService.getFCMNotificationList();
					
					for( FCMNotification fcm : lNotif ){
						
						Service.writeDebug("----------------------------------------------------------",debugLabel);
						Service.writeDebug("ID: " + fcm.getNotificationId(),debugLabel);
						Service.writeDebug("Token: " + fcm.getFcmToken(),debugLabel);
						Service.writeDebug("Title: " + fcm.getTitle(),debugLabel);
						Service.writeDebug("Message: " + fcm.getMessage(),debugLabel);
						Service.writeDebug("Icon: " + fcm.getIcon(),debugLabel);			
						Service.writeDebug("Data : " + fcm.getData(),debugLabel);
						
						/*Start send to FCM Server*/
						JSONObject joFCMResult 				= fcmService.sendToFCM(fcm.getFcmToken(), 
																				   fcm.getTitle(), 
																				   fcm.getMessage(), 
																				   fcm.getIcon(),
																				   fcm.getData());
						
						if( joFCMResult != null ){
						
							Object json 						= objMap.readValue(joFCMResult.toString(), Object.class);
							
							Service.writeDebug("FCMResult : " + objMap.writerWithDefaultPrettyPrinter().writeValueAsString(json),debugLabel);
						
							/*Update FCM Notification Status*/
							int statusResponse 					= ( Integer.parseInt( JSON.get(joFCMResult, "success") ) == 1 ? 1 : -1 ); 
							fcmService.updateStatusResponseFCMNotification(fcm.getNotificationId(), 
																	   	   statusResponse, 
																	   	   joFCMResult.toString());
							
							fcmService.updateStatusSendNotification(fcm.getNotificationId());
							
						}else{
							fcmService.updateStatusResponseFCMNotification(fcm.getNotificationId(), 
																	   	   -1, 
																	   	   "");
									
							fcmService.updateStatusSendNotification(fcm.getNotificationId());
						}
						
						Service.writeDebug("----------------------------------------------------------",debugLabel);
					}
					
					Thread.sleep(Service.interval * 1000);
					
				}
				
			}
			
		}catch( Exception ex ){
			Service.writeDebug("Exception : " + ex.getMessage(),debugLabel);
		}
		
	}
	
	volatile boolean stop;
	final FCMNotificationThread this$0;
	private int stat;
	
	public FCMNotificationThread(int status)
	{
		super();
        this$0 = FCMNotificationThread.this;
        stop = false;
        try
        {
        	stat=status;
        	
        }catch(Exception ex){
        	Service.writeDebug("Exception : " + ex.toString(),debugLabel);
        }
	    
	}
	
}
