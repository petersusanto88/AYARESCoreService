package com.webservicemaster.iirs.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.io.MD5Hash;
import org.springframework.stereotype.Service;

@Service
public class EncryptorUtility {

	public String MD5Hashing( String value, String key ){
		
		String encryptedValue 	= "";
		value  					= value + key;
		
		MessageDigest md;
		try {
			
			md 					= MessageDigest.getInstance("MD5");
			md.update( value.getBytes() );
			
			byte byteData[] 	= md.digest();
			
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        encryptedValue = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return encryptedValue;
		
	}
	
	/*public static void main( String[] args ){
		
		String value = "123456";
		String key = "ACC374178B46135E9CF488A37F745";
		
		String encValue = EncryptorUtility.MD5Hashing( value, key );
		
		System.out.println("Result : " + encValue);
		
	}*/
	
}
