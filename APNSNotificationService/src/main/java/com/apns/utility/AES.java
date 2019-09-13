package com.apns.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
  static String IV = "AAAAAAAAAAAAAAAA";
  static String plaintext = "3306"; /*Note null padding*/
  static String encryptionKey = "QLN5xL1rs808dQdp6is2LJUrSHHuLMNiQLN5xL1rs808dQdp6is2LJUrSHHuLMNi";
                                 
  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
  /*public static void main(String [] args) {
    try {
      
      System.out.println("==Java==");
      System.out.println("plain:   " + plaintext);

      byte[] cipher = encrypt(plaintext, encryptionKey);
      
      System.out.println("Result : " + stringToHex(cipher));

      System.out.print("cipher:  ");
      for (int i=0; i<cipher.length; i++)
        System.out.print(new Integer(cipher[i])+" ");
      System.out.println("");

      String decrypted = decrypt(cipher, encryptionKey);

      System.out.println("decrypt: " + decrypted);

    } catch (Exception e) {
      e.printStackTrace();
    } 
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
    return stringToHex(cipher.doFinal(plainText.getBytes("UTF-8")));
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