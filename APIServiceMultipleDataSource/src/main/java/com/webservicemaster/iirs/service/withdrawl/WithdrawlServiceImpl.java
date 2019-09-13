package com.webservicemaster.iirs.service.withdrawl;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.webservicemaster.iirs.dao.transaction.TransactionRepository;
import com.webservicemaster.iirs.dao.transaction.WithdrawlRepository;
import com.webservicemaster.iirs.domain.master.Account;
import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.domain.master.CustomerAccountBank;
import com.webservicemaster.iirs.domain.transaction.Withdrawl;
import com.webservicemaster.iirs.service.account.AccountService;
import com.webservicemaster.iirs.service.customer.CustomerService;
import com.webservicemaster.iirs.service.customeraccountbank.CustomerAccountBankService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class WithdrawlServiceImpl implements WithdrawlService {

private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawlServiceImpl.class);
	
	@PersistenceContext(unitName="transactionPU")
	private EntityManager manager;
	
	@Autowired
	private Utility util;
	
	@Autowired
	WithdrawlRepository repo;
	
	@Autowired
	private AccountService accService;
	
	@Autowired
	private CustomerAccountBankService customerAccountBankService;
	
	@Autowired
	private CustomerService customerService;
	
	@Value("${db.key}")
	private String dbKey;
	
	@Autowired
	WithdrawlServiceImpl(WithdrawlRepository repository) {
        this.repo = repository;
    }
	
	@Transactional
	@Override
	public JSONObject transactionWithdrawl( long customerId,
											double amount) throws Exception{
		
		JSONObject joResult 		= new JSONObject();
		
		/*1. Get Account Info*/
		Account account 			= accService.getAccountDataByMemberId(customerId);
		CustomerAccountBank cab 	= customerAccountBankService.findByCustomerId(customerId);
		Customer customer 			= customerService.getCustomerDetailByCustomerId(customerId);
		
		/*2. Check if other withdrawl pending from this account*/
		List lCheckPendingWithdrawl	= repo.getPendingWithdrawl(account.getAccountId());
		
		if( Integer.parseInt( lCheckPendingWithdrawl.get(0).toString() ) > 0 ){
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "You can't withdrawl while your previous withdrawl in process by AYARES's Admin.");
			
		}else{
			
			if( amount < account.getBalance() ){
				
				/*2. Save into withdrawl*/
				Withdrawl data			= new Withdrawl();
				data.setAccountId(account.getAccountId());
				data.setAccountNumber(account.getAccountNumber());
				data.setAccountName( AES.decrypt( customer.getCustomerName(),dbKey ) );
				data.setAmount(amount);
				data.setFee(0);
				data.setPostDate(util.localTransactionTime());
				
				data.setTransferToBankCode(cab.getBankCode());
				data.setTransferToBankName(cab.getBankName());
				data.setTransferToAccountNumber(cab.getBankAccountNumber());
				data.setTransferToAccountName(cab.getBankAccountName());
				
				repo.save(data);
				repo.flush();
				
				if( data.getWithdrawlId() > 0 ){
					
					joResult.put("errCode", "00");
					joResult.put("errMsg", "Withdrawl has been save to AYARES Back Office. Our admin will process this in 24 hours.");
					
				}else{
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "Withdrawl failed.");
				}
			
			}else{
			
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Sorry, your balance is not enough for this transaction.");
			
			}
			
		}	
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getWithdrawlHistory( long accountId,
										   int start,
										   int limit){
		
		JSONObject joResult 		= new JSONObject();
		JSONArray jaData 			= new JSONArray();
		JSONObject joData			= null;
		
		String statusTransfer 		= "";
		
		String sql					= " SELECT w.withdrawlId,"
									+ " 	   DATE_FORMAT(w.postDate,'%Y%m%d%H%i%s') AS postDate,"
									+ "		   w.amount,"
									+ "		   w.fee,"
									+ "        w.statusTransfer,"
									+ "		   w.transferFromAccountNumber,"
									+ "		   w.transferFromBankName,"
									+ "		   w.transferFromAccountName,"
									+ "		   DATE_FORMAT(w.transferDate,'%Y%m%d%H%i%s') AS transferDate,"
									+ ""
									+ "		   w.transferToBankName,"
									+ "		   w.transferToAccountName,"
									+ "		   w.transferToAccountNumber"
									+ " FROM Withdrawl w"
									+ " WHERE w.accountId = :id"
									+ " ORDER BY w.postDate DESC";
		
		List<Object[]> lHistory 	= manager.createQuery(sql)
										.setParameter("id", accountId)
										.setFirstResult(start)
										.setMaxResults(limit)
										.getResultList();
		
		Iterator<Object[]> it 		= lHistory.iterator();
		
		while( it.hasNext() ){
			
			Object[] result 		= (Object[])it.next();
			joData 					= new JSONObject();
			
			if( (short)result[4] == 0 ){
				statusTransfer 		= "Pending";
			}else if( (short)result[4] == 1 ){
				statusTransfer 		= "Approved";
			}else{
				statusTransfer 		= "Rejected";
			}
			
			joData.put("withdrawlId", (Long)result[0]);
			joData.put("postDate", (String)result[1]);
			joData.put("amount", (Double)result[2]);
			joData.put("fee", (Double)result[3]);
			
			joData.put("statusTransfer", statusTransfer);
			joData.put("transferFromAccountNumber", (String)result[5]);
			joData.put("transferFromBankName", (String)result[6]);
			joData.put("transferFromAccountName", (String)result[7]);
			joData.put("transferDate", (String)result[8]);
			
			joData.put("transferToBankName", (String)result[9]);
			joData.put("transferToAccountName", (String)result[10]);
			joData.put("transferToAccountNumber", (String)result[11]);
			
			jaData.add(joData);
			
		}
		
		joResult.put("errCode", "00");
		joResult.put("errMsg", "OK");
		joResult.put("data", jaData);		
		
		return joResult;
		
	}
	
}
