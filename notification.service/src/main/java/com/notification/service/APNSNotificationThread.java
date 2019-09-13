package com.notification.service;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notifcation.service.main.APNSNotificationService;
import com.notifcation.service.main.APNSNotificationServiceImpl;
import com.notification.service.model.APNSNotification;
import com.notification.service.util.JSON;

public class APNSNotificationThread extends Thread {

	private static APNSNotificationService apnsService 		= new APNSNotificationServiceImpl();
	private static ObjectMapper objMap						= new ObjectMapper();
	private static String debugLabel						= "APNSNotificationThread";
	
	public void run(){
		
		try{
		
			if( !this.stop ){		
				
				Service.writeDebug("Starting APNSNotificationThread...",debugLabel);
				
				while(true) {
					
					List<APNSNotification> lNotif 				= apnsService.getAPNSNotificationList();
					
					for( APNSNotification apns : lNotif ) {
						
						Service.writeDebug("----------------------------------------------------------",debugLabel);
						Service.writeDebug("ID: " + apns.getApnNotificationId(),debugLabel);
						Service.writeDebug("Token: " + apns.getApnToken(),debugLabel);
						Service.writeDebug("Title: " + apns.getTitle(),debugLabel);
						Service.writeDebug("Message: " + apns.getMessage(),debugLabel);
						Service.writeDebug("Icon: " + apns.getIcon(),debugLabel);			
						Service.writeDebug("Data : " + apns.getData(),debugLabel);
						
						/*Start send to APNS Server*/
						apnsService.sendToAPNS(apns.getApnToken(), 
											   apns.getTitle(),
											   apns.getMessage(), 
											   "",JSON.newJSONObject( apns.getData() ));
						
						/*Update APNS Notification Status*/					
						apnsService.updateStatusSendNotification(apns.getApnNotificationId());
						
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
	final APNSNotificationThread this$0;
	private int stat;
	
	public APNSNotificationThread(int status)
	{
		super();
        this$0 = APNSNotificationThread.this;
        stop = false;
        try
        {
        	stat=status;
        	
        }catch(Exception ex){
        	Service.writeDebug("Exception : " + ex.toString(),debugLabel);
        }
	    
	}
	
}
