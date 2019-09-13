package com.webservicemaster.iirs.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.print.DocFlavor.STRING;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Hex;

@Service
public class AES{
	
	//static String IV = "AAAAAAAAAAAAAAAA";
	  //static String plaintext = "Mn236Y86dR56P5K81qc2Knydo16Lj67q"; /*Note null padding*/
	  //static String plaintext = "{\"trxid\":\"ALT201505292420538581\",\"username\":\"*******\",\"productid\":\"as5\",\"msisdn\":\"085263049425 \",\"ACK\":\"1\",\"datetime\":\"2015-05-29 24:20:53\",\"type\":\"TOPUP\",\"password\":\"*******\",\"signature\":\"2e68f289835404a2d4bb0c1eef66e7b0\",\"accountid\":\"******\"}";
	  /*static String plaintext = "085263049425                        000000005000AS";
	  static String encryptionKey = "E52FF0E51A55DE36";*/
	  //static String encryptionString = "0247962d4673dc47992529c0099d33c15dd65a76e2ae5929b6b430eeb9bb9fc44eed208e246e5f1d6b9cc6558b7122c5";
	                                 
	  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
	  public static void main2(String [] args) {
	    try {
	      
//	      System.out.println("==Java==");
//	      System.out.println("plain: Asiando Hutabarat");
//
//	      System.out.println(encrypt("Asiando Hutabarat", "E52FF0E51A55DE36"));
//	      
//	      System.out.println("Result : " + stringToHex(cipher));
//
//	      System.out.print("cipher:  ");
//	      for (int i=0; i<cipher.length; i++)
//	        System.out.print(new Integer(cipher[i])+" ");
//	      System.out.println("");
//
//	      String decrypted = decrypt(cipher, encryptionKey);
//
//	      System.out.println("decrypt: " + decrypted);
	    	  
	  //  String decrypted = decrypt(encryptionString, encryptionKey);
	    	
	    	String qrcode = "7826318667";				
			System.out.println("### QRCODE : " + encrypt(qrcode, "4B1C9E44D769C429"));

	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	  }
	  
	  /*public static void main( String[] args ) throws Exception{
		  
		  String strClear = "333333";
		  
		  String encrypted = encrypt(strClear, encryptionKey);
		  
		  System.out.println("Encrypted : " + encrypted);
		  
		  String decrypted = decrypt("7c0f62775a0ab137f8550aa0998ea9af090f62e3198779876569205f4f03bd42", "4B1C9E44D769C429"		);  
		  System.out.println("Decrypted : " + decrypted);
		  
	  }*/
	  
	  /*public static void main( String[] args ) throws Exception{
		  
		  String strClear = "1228371917";
		  //email|customerId|phoneNumber|method (scan on customer device)
		  
		  System.out.println("Encrypt : " + encrypt( strClear, "4B1C9E44D769C429" ));
		  
	  }*/

	  public static String stringToHex(byte[] input) throws UnsupportedEncodingException
	  {
	      if (input == null) throw new NullPointerException();
	      return asHex(input);
	  }
	  
	  private static String asHex(byte[] buf)
	  {
	      char[] chars = new char[2 * buf.length];
	      for (int i = 0; i < buf.length; ++i)
	      {
	          chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
	          chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
	      }
	      return new String(chars);
	  }
	  
	  public static String encrypt(String plainText, String encryptionKey) throws Exception {
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.ENCRYPT_MODE, key);
	    return new String(Hex.encodeHex(cipher.doFinal(plainText.getBytes("UTF-8"))));
	  }

	  public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
	    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    return new String(cipher.doFinal(cipherText),"UTF-8");
	  }

	  public static String decrypt(String encryptString, String encryptionKey) throws Exception{
	  	byte[] bytes = Hex.decodeHex(encryptString.toCharArray());
	  	String decrypted = decrypt(bytes, encryptionKey);
	    return decrypted;
	  }
	
}