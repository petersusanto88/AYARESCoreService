package com.notification.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notifcation.service.main.SMSEmailQueueServiceImpl;
import com.notification.service.model.SMSEmailQueue;

public class SMSEmailQueueThread extends Thread{

	private static SMSEmailQueueServiceImpl queueService   = new SMSEmailQueueServiceImpl();
	private static ObjectMapper objMap					   = new ObjectMapper();
	private static String debugLabel 					   = "SMSEmailQueueThread";
	
	public void run(){
		
		try{		
			
			if( !this.stop ){		
				
				Service.writeDebug("Starting SMSEmailQueueThread...", debugLabel);
				
				while( true ){
					
					List<SMSEmailQueue> lQueue 				= queueService.getSMSEmailQueue();
					
					for( SMSEmailQueue queue : lQueue ){
						
						Service.writeDebug("----------------------------------------------------------", debugLabel);
						Service.writeDebug("ID : " + queue.getId(), debugLabel);
						Service.writeDebug("Type : " + queue.getType(), debugLabel);
						Service.writeDebug("Subject : " + queue.getSubject(), debugLabel);
						Service.writeDebug("Body : " + queue.getBody(), debugLabel);
						Service.writeDebug("Start Send Email...", debugLabel);
						
						JSONObject joResultSendEmail		= queueService.sendEmail(queue.getDestinationAccount(), 
																					 queue.getSubject(), 
																					 queue.getBody(), 
																					 Service.mailHost, 
																					 Service.mailPort, 
																					 Service.mailUsername, 
																					 Service.mailPassword);
						
						Object json 						= objMap.readValue(joResultSendEmail.toString(), Object.class);
						
						Service.writeDebug("Result Send Email : " + objMap.writerWithDefaultPrettyPrinter().writeValueAsString(json), debugLabel);
						
						queueService.updateStatusSend(queue.getId());
						
						Service.writeDebug("----------------------------------------------------------", debugLabel);
						
					}
					
					Thread.sleep(Service.interval * 1000);
					
				}
				
			}
			
		}catch( Exception ex ){
			Service.writeDebug("Exception : " + ex.getMessage(), debugLabel);
		}
		
	}
	
	volatile boolean stop;
	final SMSEmailQueueThread this$0;
	private int stat;
	
	public SMSEmailQueueThread(int status)
	{
		super();
        this$0 = SMSEmailQueueThread.this;
        stop = false;
        try
        {
        	stat=status;
        	
        }catch(Exception ex){
        	Service.writeDebug("Exception : " + ex.toString(), debugLabel);
        }
	    
	}
	
}
