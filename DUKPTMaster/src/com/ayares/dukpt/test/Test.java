package com.ayares.dukpt.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.ayares.dukpt.DESCryptoUtil;
import com.ayares.dukpt.DUKPTUtil;
import com.ayares.dukpt.DukptDecrypt;
import com.ayares.dukpt.StringUtil;
import com.ayares.dukpt.Util;

public class Test {
	
	private static String bdk = "69AEBB5615BC4DF5EDD0B95275348FF0";
	private static String ksn = "FFFF9876543210E00000";

	private static DukptDecrypt dukpt = new DukptDecrypt();
	private static DUKPTUtil dukptUtil = new DUKPTUtil();
	
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// Generate IPEK/IKEY Initial
		byte[] ksnbytes = Util.hex2byte(ksn);
		byte[] bdkbytes = Util.hex2byte(bdk);
		byte[] ipek = dukpt.generateIPEK(ksnbytes, bdkbytes);
		System.out.println("IPEK : " + Util.hexString(ipek));
		
		//Generate Future Key
		
		String encryptedData = "849A6FCF8C7AFBDEA264A4C1E7D263E8";
		
		String decryptedData = dukpt.decrypt("FFFF0115830123600004", bdk, encryptedData);
		
		System.out.println("Decrypted Data : " + decryptedData);
		
	}*/
	
	public static void main( String[] args ) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		
		ksn 			= "FFFF9876543210E00000";
		byte[] bdkbytes = Util.hex2byte(bdk);		
		byte[] ksnbytes = Util.hex2byte(ksn);
		
		byte[] ipekbytes = dukptUtil.generateIPEK(ksnbytes, bdkbytes);
		
		System.out.println("------------------------------------------");
		System.out.println("			### ON AYARES ###			  ");
		System.out.println("------------------------------------------");
		System.out.println(">>> IPEK : " + StringUtil.toHexString(ipekbytes));
		
		System.out.println("------------------------------------------");
		System.out.println("			### ON TERMINAL ###			  ");
		System.out.println("------------------------------------------");
		// Derive Key 0
		ksn 			= "FFFF9876543210E00000";
		ksnbytes 		= Util.hex2byte(ksn);
		byte[] derivedKey = dukptUtil.deriveKeyOnTerminal(ksnbytes, ipekbytes);
		System.out.println(">>> FTK 0 : " + StringUtil.toHexString(derivedKey));
		
		ksn 			= "FFFF9876543210E00001";
		ksnbytes 		= Util.hex2byte(ksn);
		derivedKey 		= dukptUtil.deriveKeyOnTerminal(ksnbytes, ipekbytes);
		System.out.println(">>> FTK 1 : " + StringUtil.toHexString(derivedKey));
		
		//Encrypted Data
		String data  	= "PETERSUSANTO";
		System.out.println(">>> Clear Data : " + data);
		String dataInHex = StringUtil.asciiToHex( data );
		System.out.println(">>> ASCII To Hex : " + dataInHex);
		byte[] databytes = Util.hex2byte( dataInHex );
		byte[] encryptedData = DESCryptoUtil.tdesEncrypt(databytes, derivedKey);		
		System.out.println( ">>> Encrypted Data : " + StringUtil.toHexString(encryptedData) );
		
		System.out.println("------------------------------------------");
		System.out.println("			### ON AYARES ###			  ");
		System.out.println("------------------------------------------");
		
		byte[] deriveKeyTrans = DUKPTUtil.deriveKey(ksnbytes, bdkbytes);
		System.out.println(">>> Derived Key : " + StringUtil.toHexString(deriveKeyTrans));
		byte[] decryptedData = DESCryptoUtil.tdesDecrypt(encryptedData, deriveKeyTrans);
		System.out.println( ">>> Decrypted Data : " + StringUtil.toHexString(decryptedData) );
		
		
	}

}
