package com.webservicemaster.iirs.service.transactiondetail;

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

import com.google.gson.Gson;
import com.webservicemaster.iirs.dao.transaction.TransactionDetailRepository;
import com.webservicemaster.iirs.domain.transaction.TransactionDetail;
import com.webservicemaster.iirs.service.transaction.TransactionServiceImpl;
import com.webservicemaster.iirs.utility.JSON;

@Service
public class TransactionDetailServiceImpl implements TransactionDetailService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDetailServiceImpl.class);
	
	@PersistenceContext(unitName="transactionPU")
	private EntityManager manager;
	
	@Autowired
	private TransactionDetailRepository transRepo;
	
	@Value("${merchant.photo.icon.40x60}")
	private String merchantIcon40x60;
	
	@Autowired
	TransactionDetailServiceImpl(TransactionDetailRepository repository) {
        this.transRepo = repository;
    }
	
	@Override
	public JSONObject getTransactionHistory( long accountId, 
											 String keyword,
											 int start,
											 int limit){
		
		JSONObject joResult					= new JSONObject();		
		JSONObject joDataHistory 			= null;
		JSONArray jaData 					= new JSONArray();	
		String merchantIcon 				= "";
		
		//Version 1:
		/*List<Object[]> lHistory 			= transRepo.findByAccountIdDestination(accountId);
		Iterator<Object[]> it				= lHistory.iterator();
		
		while( it.hasNext() ){
			
			Object[] result 				= (Object[])it.next();
			joDataHistory 					= new JSONObject();
			
			joDataHistory.put("transactionDate", (String)result[0]);
			joDataHistory.put("transactionName", (String)result[1]);
			joDataHistory.put("total", (String)result[2]);
			joDataHistory.put("transType", (String)result[3]);	
			
			jaData.add(joDataHistory);
			
		}*/
		
		//Version 2:
		List<Object[]> lHistory 	= manager.createQuery("SELECT td.transDetailId,"
																+ " 	  td.transId,"
																+ "		  td.transactionCodeId,"
																+ "		  td.transactionName,"
																+ "		  td.accountIdDestination,"
																+ "		  td.accountNumberDestination,"
																+ "		  td.transType,"
																+ "	 	  td.total,"
																+ "		  DATE_FORMAT(td.transactionDate,'%Y%m%d%H%i%s') AS transDate,"
																+ "		  t.merchantName,"
																+ "		  t.transactionNumber,"
																+ "		  t.merchantIcon40x60"
																+ " FROM TransactionDetail td, Transaction t "
																+ " WHERE td.transId = t.transactionId"
																+ " AND td.accountIdDestination = :id"
																+ " AND ( td.transactionCodeId IN (3,4,12,13,5) )"
																+ " AND ( td.transactionName LIKE :keyword1 "
																+ "		  OR t.merchantName LIKE :keyword2 "
																+ "	      OR t.transactionNumber LIKE :keyword3 ) "
																+ " ORDER BY t.transDate DESC")
												
												     .setParameter("id", accountId)
												     .setParameter("keyword1", "%" + keyword + "%")
												     .setParameter("keyword2", "%" + keyword + "%")
												     .setParameter("keyword3", "%" + keyword + "%")
												     .setFirstResult(start)
												     .setMaxResults(limit)
										 			 .getResultList();
		
		Iterator<Object[]> it				= lHistory.iterator();	
		
		while( it.hasNext() ){
			
			Object[] result 				= (Object[])it.next();
			joDataHistory 					= new JSONObject();
			
			if( ( (String)result[9] ).compareTo("") != 0 ){
				merchantIcon 				= merchantIcon40x60 + "/" + (String)result[11];
			}else{
				merchantIcon 				= merchantIcon40x60 + "/default.jpg";
			}
			
			joDataHistory.put("transDetailId", (Long)result[0]);
			joDataHistory.put("transId", (Long)result[1]);
			joDataHistory.put("transactionCodeId", (Long)result[2]);
			joDataHistory.put("transactionName", (String)result[3]);
			joDataHistory.put("accountIdDestination", (Long)result[4]);
			joDataHistory.put("accountNumberDestination", (String)result[10]);
			joDataHistory.put("transType", (String)result[6]);
			joDataHistory.put("total", (Double)result[7]);
			joDataHistory.put("transDate", (String)result[8]);
			joDataHistory.put("merchantName", (String)result[9]);
			joDataHistory.put("merchantIcon", merchantIcon);
			jaData.add(joDataHistory);
			
		}
		
		joResult.put("errCode", "00");
		joResult.put("errMsg", "OK");
		joResult.put("data", jaData);
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getTransactionDetailByTransactionNumber( String transactionNumber, long accountId ){
		
		JSONObject joResult					= new JSONObject();		
		JSONObject joDataHistory 			= null;
		JSONArray jaData 					= new JSONArray();	
		
		
		List<Object[]> lHistory 	= manager.createQuery("SELECT td.transDetailId,"
																+ " 	  td.transId,"
																+ "		  td.transactionCodeId,"
																+ "		  td.transactionName,"
																+ "		  td.accountIdDestination,"
																+ "		  td.accountNumberDestination,"
																+ "		  td.transType,"
																+ "	 	  td.total,"
																+ "		  DATE_FORMAT(td.transactionDate,'%Y%m%d%H%i%s') AS transDate,"
																+ "		  t.merchantName"
																+ " FROM TransactionDetail td, Transaction t "
																+ " WHERE td.transId = t.transactionId "
																+ " AND t.transactionNumber = :transNumber"
																+ " AND td.accountIdDestination = :accountId ")
												
												     .setParameter("transNumber", transactionNumber)
												     .setParameter("accountId", accountId)
										 			 .getResultList();
		
		Iterator<Object[]> it				= lHistory.iterator();	
		
		while( it.hasNext() ){
			
			Object[] result 				= (Object[])it.next();
			
			
			
			joResult.put("transDetailId", (Long)result[0]);
			joResult.put("transId", (Long)result[1]);
			joResult.put("transactionCodeId", (Long)result[2]);
			joResult.put("transactionName", (String)result[3]);
			joResult.put("accountIdDestination", (Long)result[4]);
			joResult.put("accountNumberDestination", (String)result[5]);
			joResult.put("transType", (String)result[6]);
			joResult.put("total", (Double)result[7]);
			joResult.put("transDate", (String)result[8]);
			joResult.put("merchantName",(String)result[9]);
			
		}
		
		joResult.put("errCode", "00");
		joResult.put("errMsg", "OK");
		
		return joResult;
		
	}
	
}
