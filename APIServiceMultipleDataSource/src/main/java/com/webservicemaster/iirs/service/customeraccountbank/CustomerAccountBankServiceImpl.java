package com.webservicemaster.iirs.service.customeraccountbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webservicemaster.iirs.dao.master.CustomerAccountBankRepository;
import com.webservicemaster.iirs.dao.master.CustomerRepository;
import com.webservicemaster.iirs.domain.master.BankCode;
import com.webservicemaster.iirs.domain.master.CustomerAccountBank;
import com.webservicemaster.iirs.service.bankcode.BankCodeService;
import com.webservicemaster.iirs.service.customer.CustomerServiceImpl;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class CustomerAccountBankServiceImpl implements CustomerAccountBankService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountBankServiceImpl.class);
	private CustomerAccountBankRepository customerAccountBankRepo;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Autowired
	private AES aes;
	
	@Value("${ktp.photo.path}")
	private String ktpPath;
	
	@Value("${ktp.photo.url}")
	private String ktpURL;
	
	@Autowired
	private Utility util;
	
	@Autowired
	private BankCodeService bankCodeService;
	
	@Value("${photo.allowed.ext}")
	private String[] photoAllowedExt;
	
	@Autowired
	CustomerAccountBankServiceImpl(CustomerAccountBankRepository repository) {
        this.customerAccountBankRepo = repository;
    }
	
	@Override
	@Transactional
	public JSONObject updateAccountBankSetting( long customerId,
											    String bankCode,
											    String accountNumber,
											    String accountName,
											    String identityCard,
											    String fileIdentityCard){
		
		JSONObject joResult 		= new JSONObject();
				
		/*1. Check has account bank*/
		List<?> lCheck 				= customerAccountBankRepo.isAccountBankExists(customerId);
		
		/*2. Get BankCode info*/
		BankCode bankCodeData 		= bankCodeService.findByBankCode(bankCode);
		
		if( Integer.parseInt( lCheck.get(0).toString() ) > 0 ){
			
			/*Update existing*/
			int updateStatus		= customerAccountBankRepo.updateCustomerAccountBank(bankCode, 
																						bankCodeData.getBankName(),
																						accountNumber, 
																						accountName, 
																						fileIdentityCard, 
																						identityCard, 
																						customerId);
			
			if( updateStatus > 0 ){
				
				joResult.put("errCode", "00");
				joResult.put("errMsg","Successfully update account bank");
				
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg","Failed update account bank");
				
			}
			
		}else{
			
			/*Add new record*/
			CustomerAccountBank cab		= new CustomerAccountBank();
			cab.setBankAccountName(accountName);
			cab.setBankAccountNumber(accountNumber);
			cab.setBankCode(bankCode);
			cab.setBankName(bankCodeData.getBankName());
			cab.setCustomerId(customerId);
			cab.setFileIdentity(fileIdentityCard);
			cab.setIdentityCard(identityCard);
			
			customerAccountBankRepo.save(cab);
			
			if( cab.getAccountBankId() > 0 ){
				
				joResult.put("errCode", "00");
				joResult.put("errMsg","Successfully add account bank");
				
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg","Failed update account bank");
				
			}
			
		}
		
		return joResult;
		
	}
	
	@Override
	@Transactional
	public JSONObject deleteAccountBankSetting( long customerId ){
		
		JSONObject joResult 		= new JSONObject();
		
		/*1. Check has account bank*/
		List<?> lCheck 				= customerAccountBankRepo.isAccountBankExists(customerId);
		
		if( Integer.parseInt( lCheck.get(0).toString() ) > 0 ){
			
			customerAccountBankRepo.deleteCustomerAccountBank(customerId);
			
			joResult.put("errCode", "00");
			joResult.put("errMsg","Successfully delete account bank");
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg","Failed delete account bank");
			
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject uploadIdentityCard( MultipartFile file ){
		
		JSONObject joResult			= new JSONObject();
		String fileExtension 		= file.getContentType().split("/")[1];
		String fileName 			= util.generateFileName();
		String fileContentType 		= file.getContentType();
		String filePath				= ktpPath + fileName + "." + fileExtension;
		String urlFile 				= ktpURL + fileName + "." + fileExtension;		
		
		byte[] buffer 				= new byte[1000];
		File outputFile 			= new File( filePath );
		FileInputStream reader  	= null;
		FileOutputStream writer 	= null;
		int totalBytes 				= 0;
		
		try{
			/*Check ext file firs*/
			if( ArrayUtils.contains(photoAllowedExt, fileContentType) ){
				
				outputFile.createNewFile();
				reader 					= (FileInputStream)file.getInputStream();
				writer 					= new FileOutputStream(outputFile);
				int bytesRead 			= 0;
				while( ( bytesRead = reader.read( buffer ) ) != -1 ){
					
					writer.write(buffer);
					totalBytes += bytesRead;
					
				}
				
				joResult.put("errCode","00");
				joResult.put("errMsg", "OK");
				joResult.put("filePath", urlFile);
				joResult.put("fileName", fileName + "." + fileExtension);
				joResult.put("mimeType", file.getContentType());
			}else{
				
				joResult.put("errCode","-99");
				joResult.put("errMsg", "Upload file failed. Not allowed extension.");
				
			}
			
		}catch( IOException ioEx ){
			LOGGER.debug("[CustomerServiceImpl] IOException : " + ioEx.getMessage());
			joResult.put("errCode","-99");
			joResult.put("errMsg", "Upload file failed. Err: " + ioEx.getMessage());
		}finally{
			
			try{
				reader.close();
				writer.close();
			}catch(IOException ioEx){
				LOGGER.debug("[CustomerServiceImpl] IOException : " + ioEx.getMessage());
				joResult.put("errCode","-99");
				joResult.put("errMsg", "Upload file failed. Err: " + ioEx.getMessage());
			}
			
		}
		
		return joResult;
		
	}
	
	@Override
	public CustomerAccountBank findByCustomerId( long customerId ){
		
		return customerAccountBankRepo.findByCustomerId(customerId);
		
	}
}
