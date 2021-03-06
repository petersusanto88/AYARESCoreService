package com.notification.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.notification.service.util.Util;

public class Service {

	private static Properties props 					= null;
	public static int timeout 							= 0;
    public static int interval 							= 1;
    private static boolean debug 						= false;
    private static FileOutputStream fos 				= null;
    private static String fileDate 						= "";
    private static String debugFilename 				= "";
    public static OutputStream out;
    
    public static Util util 							= new Util();
    
    private static String dbHost 						= "";
    private static int dbPort							= 0;
    private static String dbName 						= "";
    private static String dbUser 						= "";
    private static String dbPassword 					= "";
    
    public static String fcmHost 						= "";
    public static String fcmKey 						= "";
    
    // Development 
     //private static String config 						= "config_ayares_notification.ini";
     //public static String pathCert 						= "/opt/ayares/webservice/apns_cert/aps_dev_credentials.p12";	
     
    // Production
     private static String config 						= "config_ayares_notification.ini";
     public static String pathCert 						= "/opt/ayares/webservice/apns_cert/aps_live_credentials.p12";	
   
    //Localhost
//    private static String config 						= "D:\\IIRS\\JAR\\config_ayares_notification.ini";
//    public static String pathCert 						= "D:\\IIRS\\Docs\\Technical\\MOBILE\\iOS\\APNS\\carasettingapns\\AyaresPushDev.p12";
    
    public static String mailHost 						= "";
    public static String mailUsername					= "";
    public static String mailPassword 					= "";
    public static int mailPort 							= 0;
    
    public static int runOn								= 0; // 1=>Dev, 2=>Prod 
    
    private static String debugLabel				 	= "Service";
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
		Service service = new Service(args);
    }
    
    public Service(String as[]){
    	
    	try{
    	
    		getConfiguration(config);	
	    	
	    	fileDate				= util.currentDate();
			debug 					= true;			
			debugFilename			= props.getProperty("debugFilename");
			interval				= Integer.parseInt( props.getProperty("interval") );
			
			dbHost 					= props.getProperty("dbHost");
			dbPort 					= Integer.parseInt( props.getProperty("dbPort") );
			dbName 					= props.getProperty("dbName");
			dbUser 					= props.getProperty("dbUser");
			dbPassword 				= props.getProperty("dbPassword");
			
			fcmHost					= props.getProperty("fcmHost");
			fcmKey 					= props.getProperty("fcmKey");
			
			mailHost 				= props.getProperty("mailHost");
			mailUsername	 		= props.getProperty("mailUsername");
			mailPassword 			= props.getProperty("mailPassword");
			mailPort 				= Integer.parseInt( props.getProperty("mailPort") );
			
			runOn 					= Integer.parseInt( props.getProperty("runOn") );
			
			openFileDebug();
			writeDebug("----------------------------------------------------------------",debugLabel);
	        writeDebug((new StringBuilder()).append("AYARES NOTIFICATION SERVICE, started at : ").append(util.currentDateTime()).toString(), debugLabel);
	        writeDebug("----------------------------------------------------------------", debugLabel);	
	       
	        
	        FCMNotificationThread fcmt = new FCMNotificationThread(1);
	        fcmt.start();
	        
	        SMSEmailQueueThread smsEmailT = new SMSEmailQueueThread(1);
	        smsEmailT.start();
	        
	        APNSNotificationThread apnsThread  = new APNSNotificationThread(1);
	        apnsThread.start();
	        
    	}catch(Exception exception)
        {
        	Service.writeDebug("Exception :  " + exception.toString(), debugLabel);   	
        }
    	
    }
    
    public static void writeDebug(String s, String label){
		try{
			
			System.out.println("[" + label + "] " + s);
			if(out != null)
				out.write((new StringBuilder()).append("[" + label + "] ").append(s).append("\r\n").toString().getBytes());
		
		
			if(fileDate.compareTo(util.currentDate()) != 0){
				try{
					if(fos != null)
					  fos.close();
				}catch(Exception exception){
					System.out.println((new StringBuilder()).append("open debug file error : ").append(exception.toString()).toString());
				}
				fileDate = util.currentDate();
				openFileDebug();
			}
			s = (new StringBuilder()).append(util.currentDateTime()).append(" : ").append(s).append("\r\n").toString();
			try{
				fos.write(s.getBytes());
			}
			catch(IOException ioexception){
				System.out.println((new StringBuilder()).append("Error writing to debug file : ").append(ioexception.toString()).toString());
			}
		
		
		}catch(Exception exception1) { }
	}
    
	public static void writeDebug(String s, int i)
    {
        try
        {
        	
            System.out.println(s);
            if(i == 0 && out != null)
                out.write((new StringBuilder()).append(s).append("\r\n").toString().getBytes());
        }
        catch(Exception exception) { }
    }
	
	private static void openFileDebug()
    {
        try
        {
            int i = debugFilename.lastIndexOf(".");
            String s = (new StringBuilder()).append(debugFilename.substring(0, i)).append(fileDate).append(debugFilename.substring(i)).toString();
            System.out.println("debugfilename  " + s);
            fos = new FileOutputStream(s, true);
        }
        catch(IOException ioexception)
        {
            System.out.println((new StringBuilder()).append("Open File for Debug Log error : ").append(ioexception.toString()).toString());
        }
    }
	
	private void getConfiguration(String s)
    {
        if(s != null)
        {
            File file = new File(s);
            try
            {
                FileInputStream fileinputstream = new FileInputStream(file);
                props = new Properties();
                props.load(fileinputstream);
                fileinputstream.close();
            }
            catch(FileNotFoundException filenotfoundexception)
            {
                System.err.println(filenotfoundexception);
                Service.writeDebug("Exception getConfiguration :  " + filenotfoundexception, debugLabel);
                System.exit(1);
            }
            catch(IOException ioexception)
            {
                System.err.println(ioexception);
                Service.writeDebug("Exception getConfiguration :  " + ioexception, debugLabel);
                System.exit(1);
            }
        }
    }
	
}
