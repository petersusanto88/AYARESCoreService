package com.webservicemaster.iirs.service.account;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.AccountRepository;
import com.webservicemaster.iirs.domain.master.Account;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	private AccountRepository accountRepo;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Autowired
	AccountServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }
	
	private String generateAccountNumber(){
		
		String accountNumber 				= "";
		String statusSP 					= "";
		String msgSP 						= "";
		
		List<Object[]> lAccountNumber 		= manager.createNativeQuery("CALL sp_generate_account_number()")
												.getResultList();
		
		Iterator<Object[]> it 				= lAccountNumber.iterator();
		
		while( it.hasNext() ){
			
			Object[] result 				= (Object[])it.next();
			statusSP 						= (String)result[0];
			msgSP 							= (String)result[1];
			accountNumber 					= (String)result[2];
		}		
		
		return accountNumber;
		
	}
	
	@Transactional
	@Override
	public JSONObject addAccount( String accountCategory, long customerId ){
		
		JSONObject joResult 		= new JSONObject();		
		Account accData 			= new Account();
		
		String accountNumber 		= this.generateAccountNumber();
		
		accData.setAccountNumber( accountNumber );
		accData.setAccountCategory(accountCategory);
		accData.setMemberId(customerId);
		accData.setStatus( (short)1 );
		
		accountRepo.save(accData);
		
		if( accData.getAccountId() > 0 ){
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			joResult.put("accountId", accData.getAccountId());
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Generate account failed.");
			
		}
		
		return joResult;
		
	}
	
	@Override
	public boolean validateAccountNumber( String accountNumber ){
		
		boolean flagValidate 		= false;
		
		List list 					= accountRepo.validateAccountNumber(accountNumber);
		
		if( Integer.parseInt( list.get(0).toString() ) > 0 ){
			flagValidate			= true;
		}
		
		return flagValidate;
		
	}
	
	@Override
	public List<Account> getAccountDataByAccountNumber( String accountNumber ){
		
		List<Account> lAccount 		= accountRepo.findByAccountNumberAndAccountCategory(accountNumber, "customer");
		
		return lAccount;
		
	}
	
	@Override
	public Account getAccountDataByMemberId( long memberId ){		
		
		return accountRepo.findByAccountCategoryAndMemberId("customer", memberId);
		
	}
	
	@Override
	public Account getCustomerIdByAccountNumber( String accountNumber ){
		return accountRepo.findByAccountCategoryAndAccountNumber("customer", accountNumber);
	}
	
}
