package com.webservicemaster.iirs.dao.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webservicemaster.iirs.domain.transaction.Withdrawl;

@Repository
public interface WithdrawlRepository extends JpaRepository<Withdrawl, Long> {

	@Modifying
	@Query( value=" select count(w) "
				+ " from Withdrawl w"
				+ " where w.accountId = ?1"
				+ " and w.statusTransfer = 0 " )
	public List getPendingWithdrawl( long accountId );
	
}
