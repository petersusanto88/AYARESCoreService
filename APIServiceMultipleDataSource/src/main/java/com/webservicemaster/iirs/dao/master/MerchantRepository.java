package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Merchant;

public interface MerchantRepository extends BaseRepository<Merchant, Long> {

	void flush();
	
	@Modifying
	@Query( " select mua.userId,"
			+ "      mua.userName,"
			+ "		 m.merchantName,"
			+ "      m.logo,"
			+ "		 mb.branchName,"
			+ "	     m.merchantId "
		  + " from MerchantUserAccess mua, Merchant m, MerchantBranches mb "
		  + " where mua.merchantId = m.merchantId"
		  + " and mua.branchId = mb.branchId "
		  + " and mua.userLogin = ?1"
		  + " and mua.userPassword = ?2 " )
	public List<Object[]> authLogin( String userLogin, String userPassword );
	
	@Modifying
	@Query( value=" update MerchantUserAccess mua "
				+ " set mua.sessionToken = ?1,"
				+ " 	mua.tokenExpired = ?2"
				+ " where mua.userId = ?3 " )
	public int setSessionToken( String sessionToken, Date tokenExpired, int userId );
	
}