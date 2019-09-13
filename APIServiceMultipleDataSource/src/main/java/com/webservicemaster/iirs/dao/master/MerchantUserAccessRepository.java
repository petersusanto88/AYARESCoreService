package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.MerchantUserAccess;


public interface MerchantUserAccessRepository extends BaseRepository<MerchantUserAccess, Integer> {

	@Modifying
	@Query( value=" update MerchantUserAccess mua "
				+ " set mua.sessionToken = ?1,"
				+ " 	mua.tokenExpired = ?2"
				+ " where mua.userId = ?3 " )
	public int setSessionToken( String sessionToken, Date tokenExpired, int userId );
	
	@Modifying
	@Query( value=" select count(mua) "
				+ " from MerchantUserAccess mua"
				+ " where mua.sessionToken = ?1"
				+ " and mua.userId = ?2 " )
	public List tokenAvailableByCustomerId( String token, int userId );
	
	
	@Modifying
	@Query( value=" select case when tokenExpired > ?1 then 0 else 1 end"
				+ " from MerchantUserAccess mua"
				+ " where mua.userId = ?2 " )
	public List isTokenExpired( Date now, int userId );
	
	@Modifying
	@Query( value= " select m.merchantName,"
				 + "        m.logo,"
				 + "	    ( case when mb.address is null then '' else mb.address end ),"
				 + "		( case when mb.latitude is null then '' else mb.latitude end ),"
				 + "		( case when mb.longitude is null then '' else mb.longitude end )"
				 + " from Merchant m, MerchantUserAccess mua, MerchantBranches mb"
				 + " where m.merchantId = mua.merchantId"
				 + " and m.merchantId = mb.merchantId"
				 + " and mua.userId = ?1 " )
	public List<Object[]> getMerchantInfoByUserId( int userId );
	
}
