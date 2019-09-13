package com.apns.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import org.json.simple.JSONObject;

import com.apns.main.Service;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;

import io.netty.util.concurrent.Future;

public class Util {
	
	private static String debugLabel 				= "Util";
	
	public Date getDateTime() throws ParseException{
		
		Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        return simpledateformat.parse(simpledateformat.format(date));
				
	}

	public String deleteWhiteSpace(String s)
    {
        s = s.replaceAll("\r", "");
        s = s.replaceAll("\n", "");
        s = s.replaceAll("\r\n", "");
        s = s.replaceAll("\t", "");
        s = s.trim();
        return s;
    }

    public boolean isTime(String s)
    {
        String as[] = s.split("\\:");
        Calendar calendar = Calendar.getInstance();
        calendar.set(9, 0);
        if(as[0] != null)
            calendar.set(10, Integer.parseInt(as[0]));
        if(as[1] != null)
            calendar.set(12, Integer.parseInt(as[1]));
        if(as.length > 2 && as[2] != null)
            calendar.set(13, Integer.parseInt(as[2]));
        Calendar calendar1 = Calendar.getInstance();
        return calendar.before(calendar1);
    }

    public String nextTimeInterval(int i)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long l = date.getTime();
        l += i * 1000;
        date.setTime(l);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("kk:mm");
        return simpledateformat.format(date);
    }

    public String currentDateTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
        return simpledateformat.format(date);
    }

    public String currentDate()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        return simpledateformat.format(date);
    }

    public String localTransactionTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMddkkmmss");
        return simpledateformat.format(date);
    }
    
    public String getTimestamp()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("YYMMddkkmmss");
        return simpledateformat.format(date);
    }

    public String stripMSISDN(String s)
    {
        String s1 = "";
        if(s.substring(0, 1).compareTo("+") == 0)
            s1 = s.substring(3);
        else
        if(s.substring(0, 1).compareTo("0") == 0)
            s1 = s.substring(1);
        else
        if(s.substring(0, 2).compareTo("62") == 0)
            s1 = s.substring(2);
        else
            s1 = s;
        return s1;
    }

    public String formatMSISDN(String s)
    {
        String s1 = "";
        if(s.substring(0, 1).compareTo("+") == 0)
            s1 = (new StringBuilder()).append("0").append(s.substring(3)).toString();
        else
        if(s.substring(0, 2).compareTo("62") == 0)
            s1 = (new StringBuilder()).append("0").append(s.substring(2)).toString();
        else
            s1 = s;
        return s1;
    }
    
    
    public String printDataBinding( int iterate ){
    	
    	String tanda = "?";
      	for( int v=1; v<iterate; v++ ) tanda += ",?";
      	return tanda;	
    	
    }
    
    public static byte[] hexToByte(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
    
    public static String constructField57( String TXN_ID,
    									   String TXN_TOKEN,
    									   String TXN_TYPE,
    									   String TXN_CHANNEL,
    									   String TID,
    									   String MID,
    									   String CARD_ID,
    									   String CARD_TYPE,
    									   String AMOUNT_TRANSACTION,
    									   String CARD_BALANCE){
    	
    	return TXN_ID + ";" + 
    		   TXN_TOKEN + ";" + 
    		   TXN_TYPE + ";" + 
    		   TXN_CHANNEL + ";" + 
    		   TID + ";" + 
    		   MID + ";" + 
    		   CARD_ID + ";" + 
    		   CARD_TYPE + ";" + 
    		   AMOUNT_TRANSACTION + ";" + 
    		   CARD_BALANCE;
    	
    }
    
    public static JSONObject parseField57( String field57 ){
    	
    	//Format : 
    	//TXN_ID;TXN_TOKEN;TXN_TYPE;TXN_CHANNEL;TID;MID;CARD_ID;UID;KEY_INDEX;CARD_TYPE;AMONT;CARD_BALANCE
    	
    	JSONObject joResult			= new JSONObject();    	
    	
    	try{
	    	String[] f57 				= field57.split(";");
	    	String cardUID 				= f57[1].substring(f57[1].length() - 16, f57[1].length());
	    	
	    	joResult.put("TXN_ID", f57[0]);
	    	joResult.put("TXN_TOKEN", f57[1]);
	    	joResult.put("TXN_TYPE", f57[2]);
	    	joResult.put("TXN_CHANNEL", f57[3]);
	    	joResult.put("TID", f57[4]);
	    	joResult.put("MID", f57[5]);
	    	joResult.put("CARD_ID", f57[6]);
	    	joResult.put("CARD_TYPE", f57[7]);
	    	joResult.put("AMOUNT", f57[8]);
	    	joResult.put("CARD_BALANCE", f57[9]);
	    	joResult.put("CARD_UID", cardUID);
    	}catch( Exception ex ){
    		
    		System.out.println("Exception parseField57 : " + ex.getMessage());
    		
    	}
    	
    	return joResult;
    	
    }
    
    public String constructField48( String refNumber,
    								String fromCardNumber,
    								String toCardNumber,
    								String topUpAmount,
    								String lastBalance,
    								String topUpPendingBalance,
    								String currentBalance,
    								String statusDebit){
    	
    	String f48			= "";
    	
    	for( int i = refNumber.length() + 1; i <= 18; i++ ){
    		refNumber				= ( new StringBuilder() ).append(refNumber).append(" ").toString();
    	}
    	
    	for( int i = fromCardNumber.length() + 1; i <= 19; i++ ){
    		fromCardNumber			= ( new StringBuilder() ).append(fromCardNumber).append(" ").toString();
    	}
    	
    	for( int i = toCardNumber.length() + 1; i <= 19; i++ ){
    		toCardNumber			= ( new StringBuilder() ).append(toCardNumber).append(" ").toString();
    	}
    	
    	for( int i = topUpAmount.length() + 1; i <= 12; i++ ){
    		topUpAmount				= ( new StringBuilder() ).append("0").append(topUpAmount).toString();
    	}
    	
    	for( int i = lastBalance.length() + 1; i <= 12; i++ ){
    		lastBalance				= ( new StringBuilder() ).append("0").append(lastBalance).toString();
    	}
    	
    	for( int i = topUpPendingBalance.length() + 1; i <= 12; i++ ){
    		topUpPendingBalance		= ( new StringBuilder() ).append("0").append(topUpPendingBalance).toString();
    	}
    	
    	for( int i = currentBalance.length() + 1; i <= 12; i++ ){
    		currentBalance			= ( new StringBuilder() ).append("0").append(currentBalance).toString();
    	}
    	
    	f48							= ( new StringBuilder() ).append( refNumber )
    														 .append( fromCardNumber )
    														 .append( toCardNumber )
    														 .append( topUpAmount )
    														 .append( lastBalance )
    														 .append( topUpPendingBalance )
    														 .append( currentBalance )
    														 .append( statusDebit )
    														 .toString();
    	
    	return f48;
    	
    }
    
    public static String sendFCM(String url_api, 
    							 String body_content, 
    							 String content_type,
    							 String fcmKey)
    {
    	String msg="";
		String s3 = "";
		
		String request = url_api;
		
		try
        {
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", content_type); 
			connection.setRequestProperty("Authorization", "key=" + fcmKey);
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(body_content.getBytes().length));
			connection.setUseCaches (false);
			OutputStream outputstream = connection.getOutputStream();
            outputstream.write(body_content.getBytes());
            outputstream.close();
            int i = connection.getResponseCode();
            if(i == 200)
            {
                InputStream inputstream = connection.getInputStream();
                int j = connection.getContentLength();
                if(j > 0)
                {
                    byte abyte0[] = new byte[j];
                    int k;
                    for(int j1 = 0; (k = inputstream.read()) != -1; j1++)
                        abyte0[j1] = (byte)k;

                    s3 = new String(abyte0);
                } else
                {
                    char c = '\u0800';
                    byte abyte1[] = new byte[c];
                    int k1 = 0;
                    do
                    {
                        int i1;
                        if((i1 = inputstream.read()) == -1)
                            break;
                        abyte1[k1] = (byte)i1;
                        if(++k1 == c)
                        {
                            s3 = (new StringBuilder()).append(s3).append(new String(abyte1)).toString();
                            abyte1 = new byte[c];
                            k1 = 0;
                        }
                    } while(true);
                    if(k1 < c)
                        s3 = (new StringBuilder()).append(s3).append(new String(abyte1)).toString();
                }
            }
            s3 = s3.trim();
            msg=s3;
            //billing.writeDebug((new StringBuilder()).append("Request response : ").append(s3).toString());
            
            
        }catch(Exception ex){
        	System.out.println("Exception <sendRequest> : " + ex.getMessage());
        }
		
		return msg;
	}
    
//    public static String sendAPNS_1(String apnToken,
//    							 String title,
//    							 String body) {
//    	
//    	String result 							= "";
//    	boolean flagNext 						= false;
//    	
//    	ApnsServiceBuilder serviceBuilder		= APNS.newService();
//    	
//    	Service.writeDebug("### Run On : " + Service.runOn, debugLabel);
//    	
//    	if( Service.runOn == 1 ) {
//    		
//    		Service.writeDebug("Start 1", debugLabel);
//    		//String certPath						= Service.class.getResource("/opt/ayares/webservice/apns_cert/AyaresPushDev.pem").getPath();
//    		serviceBuilder.withCert("/opt/ayares/webservice/apns_cert/AyaresPushDev.p12", "").withSandboxDestination();
//    		flagNext							= true;
//    		Service.writeDebug("End 1", debugLabel);
//    		
//    	}else if( Service.runOn == 2 ) {
//    		
//    		String certPath						= Service.class.getResource("/opt/ayares/webservice/apns_cert/AyaresPushLive.pem").getPath();
//    		serviceBuilder.withCert(certPath, "").withProductionDestination();
//    		flagNext							= true;
//    		
//    	}else {
//    		Service.writeDebug("variable runOn not set", debugLabel);
//    	}
//    	
//    	if( flagNext ) {
//    		
//    		Service.writeDebug("Flag Next", debugLabel);
//    		
//    		ApnsService service 				= serviceBuilder.build();
//    		
//    		Service.writeDebug("After ApnsService", debugLabel);
//    		
//    		// Payload with custom fields
//    		String payload 						= APNS.newPayload()
//    												.alertBody(body)
//    												.alertTitle(title)
//    												.sound("default")
//    												.build();
//    		Service.writeDebug("After Payload", debugLabel);
//    		
//    		service.push(apnToken, payload);
//    		
//    		Service.writeDebug("After Push", debugLabel);
//    		
//    		
//    		
//    	}
//    	
//    	return result;
//    	
//    }
    
    
//    public static String sendAPNS(String apnToken,
//								  String title,
//								  String body) throws UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
//    	
//    	String result = "";
//    	
//    	FileInputStream cert = new FileInputStream(Service.pathCert);
//    	
//    	final ApnsClient client = new ApnsClientBuilder()
//    	        .withDevelopmentGateway()
//    	        .inSynchronousMode()
//    	        .withCertificate(cert)
//    	        .withPassword("")
//    	        .withDefaultTopic("")
//    	        .build();
//    	
//    	Notification n = new Notification.Builder(apnToken)
//    						.alertBody(body)
//    						.alertTitle(title).build();
//    	
//    	NotificationResponse resultNotif = client.push(n);
//    	Service.writeDebug(resultNotif.toString(), debugLabel);
//    	result = resultNotif.toString();
//    	
//    	return result;
//    	
//    }
    
    public static JSONObject sendAPNS(String apnToken,
								  	  String title,
								  	  String body,
								  	  JSONObject joData) throws SSLException, IOException {
    	
    	JSONObject joResult = new JSONObject();
    	final SimpleApnsPushNotification pushNotification;
    	final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
    	final String payload;
    	final String token;
    	String responseMsg;
    	
    	
    	final ApnsClient apnsClient = new ApnsClientBuilder()
    	        .setClientCredentials(new File(Service.pathCert), "ayares123")
    	        /*.setApnsServer("api.development.push.apple.com", 443)*/
    	        .setApnsServer("api.push.apple.com", 443)
    	        .build();
    	
    	 payloadBuilder.setAlertBody(body);
    	 payloadBuilder.setSoundFileName(payloadBuilder.DEFAULT_SOUND_FILENAME);
    	 payloadBuilder.addCustomProperty("data", joData);
    	 payload = payloadBuilder.buildWithDefaultMaximumLength();
    	 
    	 token = TokenUtil.sanitizeTokenString(apnToken);
    	 
    	 pushNotification = new SimpleApnsPushNotification(token, "com.ayares.user", payload);
    	 
    	 final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);
    	 
    	 try {
		    final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
		            sendNotificationFuture.get();

		    if (pushNotificationResponse.isAccepted()) {
		    	responseMsg = "Push notification accepted by APNs gateway.";
		        Service.writeDebug(responseMsg,debugLabel);
		        joResult.put("errCode", "00");
		        joResult.put("errMsg", responseMsg);
		    } else {
		    	
		    	responseMsg = "Notification rejected by the APNs gateway: " + pushNotificationResponse.getRejectionReason();
		    	Service.writeDebug(responseMsg, debugLabel);
		    	joResult.put("errCode", "-99");
			    joResult.put("errMsg", responseMsg);

		        if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
		        	
		        	responseMsg = "\t…and the token is invalid as of " + pushNotificationResponse.getTokenInvalidationTimestamp();
		        	Service.writeDebug("\t…and the token is invalid as of " + pushNotificationResponse.getTokenInvalidationTimestamp(), debugLabel);
		        	
		        	joResult.put("errCode", "-99");
				    joResult.put("errMsg", responseMsg);
		        
		        }
		    }
		} catch (final ExecutionException e) {
			responseMsg = "Failed to send push notification.";
			Service.writeDebug(responseMsg, debugLabel);
			joResult.put("errCode", "-99");
		    joResult.put("errMsg", responseMsg);
		    e.printStackTrace();
		} catch (InterruptedException e) {
			responseMsg = "Interupted Exception : " + e.getMessage();
			Service.writeDebug(responseMsg, debugLabel);
			joResult.put("errCode", "-99");
		    joResult.put("errMsg", responseMsg);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return joResult;
    	
    }
    
    
    public static String campaignSessionId()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMddkkmmss");
        return simpledateformat.format(date);
    }
	
    public static String getCurrentTimestamp(){
    	Timestamp tStamp 	= new Timestamp(System.currentTimeMillis());
    	return Long.toString( tStamp.getTime() );
    }
    
}
