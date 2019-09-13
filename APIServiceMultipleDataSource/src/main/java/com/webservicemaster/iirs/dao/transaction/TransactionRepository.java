package com.webservicemaster.iirs.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.webservicemaster.iirs.domain.transaction.Transaction;

public interface TransactionRepository extends BaseRepository<Transaction, Long>{

	/*@Modifying
	@Query(" select td.transDate,"
			+ "		td.transactionName,"
			+ "		td.total,"
			+ "		td.transType "
		 + " from TransactionDetail td"
		 + " where td.accountIdDestination = ?1 ")
	public List<Object[]> getTransactionHistory( long accountId );*/
	
}
