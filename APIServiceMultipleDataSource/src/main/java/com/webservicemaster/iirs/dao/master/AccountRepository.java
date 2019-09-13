package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Account;

public interface AccountRepository extends BaseRepository<Account, Long> {

	void flush();
	
	Account save( Account data );
	
	@Modifying
	@Query( value=" select count(a)"
				+ " from Account a"
				+ " where a.accountNumber = ?1 " )
	public List validateAccountNumber( String accountNumber );
	
	public List<Account> findByAccountNumberAndAccountCategory( String accountNumber, String accountCategory );
	
	public Account findByAccountCategoryAndMemberId( String category, long memberId );
	
	public Account findByAccountCategoryAndAccountNumber( String category, String accountNumber );
	
}
