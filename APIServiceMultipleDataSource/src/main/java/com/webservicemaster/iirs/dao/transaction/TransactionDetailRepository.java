package com.webservicemaster.iirs.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webservicemaster.iirs.domain.transaction.TransactionDetail;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {

	public List<Object[]> findByAccountIdDestination( long accountId );
	
}
