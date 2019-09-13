package com.webservicemaster.iirs.utility;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.*;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.net.MethodType;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;

@Service
public class Utility {

	public static String sendRequest(String url_api, 
								     String body_content, 
								     String content_type)
    {
    	String msg="";
		String s3 = "";
		InputStream inputstream = null;
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
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(body_content.getBytes().length));
			connection.setUseCaches (false);
			OutputStream outputstream = connection.getOutputStream();
            outputstream.write(body_content.getBytes());
            outputstream.close();
            int i = connection.getResponseCode();
            if(i == 200)
            {
                inputstream = connection.getInputStream();
            }else {
            	inputstream = connection.getErrorStream();
            }
            
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
            
            s3 = s3.trim();
            msg=s3;
            //billing.writeDebug((new StringBuilder()).append("Request response : ").append(s3).toString());
            
            
        }catch(Exception ex){
        	System.out.println("Exception <sendRequest> : " + ex.getMessage());
        }
		
		return msg;
	}
	
	public String sendGet( String url ) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		BufferedReader in = null;

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		if( responseCode == 200 ) {
			in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
		}else {
			in = new BufferedReader(
			        new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}
	
	public static String sendFCMNotification(String url_api, 
											 String body_content, 
											 String content_type,
											 String authorization)
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
			connection.setRequestProperty("Authorization", authorization);
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
	
	public String localTransactionTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        return simpledateformat.format(date);
    }
	
	public String simpleLocalTransactionTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMddHms");
        return simpledateformat.format(date);
    }
	
	public String getBindParameter( int count )
    {
   	 String tanda = "?";
   	 for( int v=1; v<count; v++ ) tanda += ",?";
   	 return tanda;
    }
	
	public String getTransactionNumber( long logId, String branchCode, int issuerId ){
		
		String transNumber = logId + 
				 	  	     branchCode + 
				 	  	     issuerId + 
				 	  	     this.simpleLocalTransactionTime();
		
		return transNumber;
		
	}
	
	 public String generateRemittanceTraceNumber(){
	        Calendar calendar = Calendar.getInstance();
	        Date date = calendar.getTime();
	        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMddkkmmss");
	        return simpledateformat.format(date);
	 }
	 
	 public static Date addingMinutes( int minute ) throws ParseException{
	        
        Date newTime 							= new Date();
        newTime 								= DateUtils.addMinutes(newTime, minute);
        SimpleDateFormat simpledateformat 		= new SimpleDateFormat("yyyy-MM-dd H:m:s");
        
        return newTime;
		
	}
	 
	public String generateRandom( int len ){
		
	   String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	   SecureRandom rnd = new SecureRandom();
		
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
		
	}
	
	public Date getDateTime() throws ParseException{
		
		Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        return simpledateformat.parse(simpledateformat.format(date));
				
	}
	
	public Date getDate() throws ParseException{
		
		Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        return simpledateformat.parse(simpledateformat.format(date));
				
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
	
	public String stringTransactionTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHms");
        return simpledateformat.format(date);
    }
	
	public String generateFileName(){
		
		return generateRandom(7) + stringTransactionTime();
		
	}
	
	public String numberFormat( double amount, String languange, String country ){
		
		Locale locale = new Locale("in", "ID");      
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		return currencyFormatter.format(amount);
		
	}
	
	public static void DownloadImage(String search, String path) throws IOException {

	    // This will get input data from the server
	    InputStream inputStream = null;

	    // This will read the data from the server;
	    OutputStream outputStream = null;

	    try {
	        // This will open a socket from client to server
	        URL url = new URL(search);

	       // This user agent is for if the server wants real humans to visit
	        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

	       // This socket type will allow to set user_agent
	        URLConnection con = url.openConnection();

	        // Setting the user agent
	        con.setRequestProperty("User-Agent", USER_AGENT);

	        // Requesting input data from server
	        inputStream = con.getInputStream();

	        // Open local file writer
	        outputStream = new FileOutputStream(path);

	        // Limiting byte written to file per loop
	        byte[] buffer = new byte[2048];

	        // Increments file size
	        int length;

	        // Looping until server finishes
	        while ((length = inputStream.read(buffer)) != -1) {
	            // Writing data
	            outputStream.write(buffer, 0, length);
	        }
	    } catch (Exception ex) {
	        System.out.println("Exception : " + ex.getMessage());
	     }

	     // closing used resources
	     // The computer will not be able to use the image
	     // This is a must

	     outputStream.close();
	     inputStream.close();
	}
	
	public static String getLastBitFromUrl(final String url){
	    // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
	    return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
	
	public String sendOAuth1( String consumerKey,
							String consumerSecret,
							String accessToken,
							String accessSecret,
							String customURIString) throws FMSException {
		
		int index = 0;
		OAuthConsumer oAuthConsumer;
		String methodtype 				= "GET";
		String output 					= "";
		
		oAuthConsumer 					= new CommonsHttpOAuthConsumer( consumerKey, consumerSecret );
		oAuthConsumer.setTokenWithSecret( accessToken, accessSecret);
		oAuthConsumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
		
		DefaultHttpClient httpClient 	= new DefaultHttpClient();
		httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		
		HttpRequestBase httpRequest = null;
		URI uri 						= null;
		
		try {
            uri = new URI(customURIString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
		
		if (methodtype.equals(MethodType.GET.toString())) {
            httpRequest = new HttpGet(uri);
        }
		
		try {
            oAuthConsumer.sign(httpRequest);
        } catch (OAuthMessageSignerException e) {
            throw new FMSException(e);
        } catch (OAuthExpectationFailedException e) {
            throw new FMSException(e);
        } catch (OAuthCommunicationException e) {
            throw new FMSException(e);
        }
		
		HttpResponse httpResponse = null;
		
		try {
            HttpHost target = new HttpHost(uri.getHost(), -1, uri.getScheme());
            httpResponse = httpClient.execute(target, httpRequest);
            System.out.println("Connection status : " + httpResponse.getStatusLine());

            InputStream inputStraem = httpResponse.getEntity().getContent();

            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStraem, writer, "UTF-8");
            output = writer.toString();

            System.out.println("### RESPONSE : " + output);
        }catch(Exception e){
            e.printStackTrace();
        }
		
		return output;
		
	}
	
}
