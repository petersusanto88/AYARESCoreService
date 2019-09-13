package com.webservicemaster.iirs.service.bankcode;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.BankCodeRepository;
import com.webservicemaster.iirs.dao.master.CustomerRepository;
import com.webservicemaster.iirs.domain.master.BankCode;

@Service
public class BankCodeServiceImpl implements BankCodeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BankCodeServiceImpl.class);
	private BankCodeRepository bankCodeRepo;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Autowired
	BankCodeServiceImpl(BankCodeRepository repository) {
        this.bankCodeRepo = repository;
    }
	
	@Override
	public JSONArray getBankCodeList(){
		
		JSONArray jaData 		= new JSONArray();
		JSONObject joData		= null;
		
		
		List<BankCode> lCode 	= bankCodeRepo.findAll();
		
		for( BankCode code : lCode ){
			
			joData 				= new JSONObject();
			
			joData.put("bankCode", code.getBankCode());
			joData.put("bankName", code.getBankName());
			jaData.add(joData);
			
		}
		
		return jaData;
		
	}
	
	@Override
	public BankCode findByBankCode( String bankCode ){
		return bankCodeRepo.findByBankCode(bankCode);
	}
	
}
