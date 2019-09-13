package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webservicemaster.iirs.domain.master.CustomerAccountBank;

@Repository
public interface CustomerAccountBankRepository extends BaseRepository<CustomerAccountBank, Long> {

	@Modifying
	@Query(value=" select count(cab) "
			   + " from CustomerAccountBank cab"
			   + " where customerId = ?1 ")
	public List<?> isAccountBankExists( long customerId );
	
	@Modifying
	@Query(value=" update CustomerAccountBank cab"
			   + " set cab.bankCode = ?1,"
			   + "	   cab.bankName = ?2,"
			   + "	   cab.bankAccountNumber = ?3,"
			   + "     cab.bankAccountName = ?4,"
			   + "     cab.fileIdentity = ?5,"
			   + "     cab.identityCard = ?6"
			   + " where cab.customerId = ?7 ")
	public int updateCustomerAccountBank( String bankCode,
									      String bankName,
										  String accountNumber,
										  String accountName,
										  String fileIdentity,
										  String identityCard,
										  long customerId);
	
	@Modifying
	@Query( " delete from CustomerAccountBank cab "
		  + " where cab.customerId = ?1 " )
	public void deleteCustomerAccountBank( long customerId );
	
	CustomerAccountBank findByCustomerId( long customerId );
	
}
