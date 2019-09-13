package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Customer;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Customer;

public interface CustomerRepository extends BaseRepository<Customer, Long> {

	List<Customer> findAll();
	
	List<Customer> findByEmail( String email );
	Customer findByCustomerPhoneNumber( String phoneNumber );
	
	Customer findByCustomerId( long customerId );
	
	Customer save( Customer data );
	
	void flush();
	
	@Modifying
	@Query( value=" select c.customerId,"
				+ "		   c.customerName,"
				+ "		   c.userLogin,"
				+ "		   c.email,"
				+ "		   c.customerPhoneNumber,"
				+ "		   c.sessionToken,"
				+ "		   a.accountId,"
				+ "		   a.balance,"
				+ "		   c.customerPhoto,"
				+ "		   a.accountNumber,"
				+ "		   c.showVoucherNotification,"
				+ "		   c.showFriendRequestNotification,"
				+ "		   c.fcmToken,"
				+ "		   c.status,"
				+ "		   c.firstLogin,"
				+ "		   c.tokenExpired"
				+ " from Customer c, Account a"
				+ " where c.customerId = a.memberId "
				+ " and ( (c.userLogin) like ?1 or (c.email) like ?1 ) "
				+ " and c.userPassword like ?2"
				+ " and a.accountCategory like 'customer' " )
	public List<Object[]> authLogin( String uLogin, String uPassword );
	
	@Modifying
	@Query( value=" select c.customerId,"
				+ "		   c.customerName,"
				+ "		   c.userLogin,"
				+ "		   c.email,"
				+ "		   c.customerPhoneNumber,"
				+ "		   c.sessionToken,"
				+ "		   a.accountId,"
				+ "		   a.balance,"
				+ "		   c.customerPhoto,"
				+ "		   a.accountNumber,"
				+ "		   c.showVoucherNotification,"
				+ "		   c.showFriendRequestNotification,"
				+ "		   c.fcmToken,"
				+ "		   c.status,"
				+ "		   c.firstLogin,"
				+ "		   c.tokenExpired"
				+ " from Customer c, Account a"
				+ " where c.customerId = a.memberId "
				+ " and c.email like ?1"
				+ " and a.accountCategory like 'customer' " )
	public List<Object[]> authLoginViaMedsos( String uLogin );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.sessionToken = ?1,"
				+ " 	c.tokenExpired = ?2"
				+ " where c.customerId = ?3 " )
	public int setSessionToken( String sessionToken, Date tokenExpired, long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.userPassword = ?1"
				+ " where c.customerId = ?2 " )
	public int changePassword( String uPassword, long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.userPassword = ?1"
				+ " where c.email = ?2 " )
	public int changePassword( String uPassword, String email );
	
	
	@Modifying
	@Query( value=" select count(c) "
				+ " from Customer c"
				+ " where c.sessionToken = ?1"
				+ " and c.customerId = ?2 " )
	public List tokenAvailableByCustomerId( String token, long customerId );
	
	
	@Modifying
	@Query( value=" select case when tokenExpired > ?1 then 0 else 1 end"
				+ " from Customer c"
				+ " where c.customerId = ?2 " )
	public List isTokenExpired( Date now, long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
			    + " set c.isLogged = ?1,"
			    + " c.lastLogin = ?2,"
			    + " c.lastLoginVia = ?3"
			    + " where c.customerId = ?4 " )
	public int updateLoginInfo( short isLogged, String loginDate, String medsos, long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
			    + " set c.isLogged = ?1"
			    + " where c.customerId = ?2 " )
	public int updateLogoutInfo( short isLogged, long customerId );
	
	@Modifying
	@Query( value=" select count(c)"
				+ " from Customer c"
				+ " where c.email = ?1 " )
	public List isEmailExist( String email );
	
	@Modifying
	@Query( value=" select count(c)"
				+ " from Customer c"
				+ " where c.customerPhoneNumber = ?1 " )
	public List isPhoneNumberExists( String email );
	
	@Modifying
	@Query( value=" select count(c) "
				+ " from Customer c"
				+ " where UPPER(c.userLogin) like ?1 " )
	public List isUserLoginExists( String userLogin );	
	
	@Modifying
	@Query( value=" select count(c) "
				+ " from Customer c"
				+ " where UPPER(c.userLogin) like ?1"
				+ " and c.customerId != ?2 " )
	public List isUserLoginExistsForUpdate( String userLogin, long customerId );
	
	@Modifying
	@Query( value=" select count(c)"
				+ " from Customer c"
				+ " where c.customerId = ?1"
				+ " and c.userPassword = ?2 " )
	public List checkCurrentPassword( long customerId, String userPassword );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.customerName = ?1,"
				+ "     c.userLogin = ?2,"
				+ "     c.email = ?3,"
				+ "     c.customerPhoneNumber = ?4,"
				+ "		c.customerPhoto = ?5"
				+ " where c.customerId = ?6 " )
	public int updateProfile( String customerName,
							  String userLogin,
							  String email,
							  String phoneNumber,
							  String customerPhoto,
							  long customerId );
	
	@Modifying
	@Query( value=" select count(c)"
			    + " from Customer c"
			    + " where c.customerId = ?1"
			    + " and c.email = ?2"
			    + " and c.customerPhoneNumber = ?3"
			    + " and c.sessionToken = ?4 " )
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber,
							    String token);
	
	@Modifying
	@Query( value=" select count(c)"
			    + " from Customer c"
			    + " where c.customerId = ?1"
			    + " and c.email = ?2"
			    + " and c.customerPhoneNumber = ?3" )
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber);
	
	// Validate QRCode just via account number
	@Modifying
	@Query( value=" select c"
			    + " from Customer c, Account a"
			    + " where c.customerId = a.memberId "
			    + " and a.accountNumber = ?1")
	public List<Customer> validateQRCode( String qrCode );
	
	@Modifying
	@Query( value=" select count(c)"
			    + " from Customer c, Account a"
			    + " where c.customerId = a.memberId"
			    + " and a.accountCategory = ?1"
			    + " and a.accountNumber = ?2 " )
	public List validateQRCodeViaAccountNumber( String category, String accountNumber );
	
	@Modifying
	@Query( value= " select f.friendId,"
				 + "        c.customerName,"
				 + "		c.customerPhoto,"
				 + "		f.inviteDateTime "
				 + " from Friendship f, Customer c"
				 + " where f.invitatorId = c.customerId"
				 + " and f.invitedId = ?1"
				 + " and f.status = 0 " )
	public List<Object[]> getFriendRequested( long customerId );
	
	@Modifying
	@Query( value=" select count(f)"
				+ " from Friendship f"
				+ " where ( f.invitedId = ? )"
				+ " and f.status = 0 " )
	public List getTotalPendingRequest( long customerId );
	
	@Modifying
	@Query( value=" select count(f)"
				+ " from Friendship f"
				+ " where ( f.invitatorId = ?1 "
				+ " or ( f.invitedId = ?1 and f.invitatorId <> 0) ) "
				+ " and f.status = 1"
				+ " and f.type <> 'special_just_friend' " )
	public List getTotalFriendList( long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.showVoucherNotification = ?1"
				+ " where c.customerId = ?2 " )
	public int updateVoucherNotification( short status, long customerId  );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.showFriendRequestNotification = ?1"
				+ " where c.customerId = ?2 " )
	public int updateFriendRequestNotification( short status, long customerId );

	@Modifying
	@Query( value=" select evp.productName,"
				+ "		   evp.photo,"
				+ "		   evp.voucherValue,"
				+ "		   evc.voucherCode,"
				+ "		   evc.startDate,"
				+ "		   evc.endDate,"
				+ "		   evp.voucherId"
				+ " from EVoucherCustomer evc, EVoucherProduct evp"
				+ "	,Customer c "
				+ " where evc.voucherId = evp.voucherId "
				+ " and evc.customerId = c.customerId "
				+ " and evc.customerId = ?1"
				+ " and evc.status = 1 " )
	public List<Object[]> getCustomerEVoucher( long customerId );
	
	@Modifying
	@Query( value=" select count(c) "
				+ " from Customer c"
				+ " where c.customerId = ?1" )
	public List validateByCustomerId( long customerId);	
	
	@Modifying
	@Query( value=" select count(c)"
				+ " from Customer c"
				+ " where c.email = ?1"
				+ " and c.customerId != ?2 " )
	public List validateEmailForUpdate( String email, long customerId );
	
	@Modifying
	@Query( value=" select count(c)"
				+ " from Customer c"
				+ " where c.customerPhoneNumber = ?1"
				+ " and c.customerId != ?2 " )
	public List validatePhoneNumberForUpdate( String phoneNumber, long customerId );
	
	//public Customer getCustomerByCustomerId( long customerId );
	
	@Modifying
	@Query(value=" select c.customerName,"
			   + "		  coalesce(c.customerPhoto,'') AS customerPhoto"
			   + " from Customer c, Account a"
			   + " where c.customerId = a.memberId"
			   + " and a.accountCategory = 'customer'"
			   + " and a.accountNumber = ?1")
	public List<Object[]> getFriendDetailByAccountNumber( String accountNumber );
	
	@Modifying
	@Query(value=" select c.customerName,"
			   + "		  coalesce(c.customerPhoto,'') AS customerPhoto"
			   + " from Customer c"
			   + " where c.customerPhoneNumber = ?1")
	public List<Object[]> getFriendDetailByPhoneNumber( String phoneNumber );
	
	@Modifying
	@Query( value= " select c.customerName,"
				 + "        coalesce(c.customerPhoto,'') AS customerPhoto,"
				 + "		c.customerPhoneNumber,"
				 + "		c.email,"
				 + "	    a.accountNumber"
				 + " from Customer c, Account a"
				 + " where c.customerId = a.memberId"
				 + " and a.accountCategory = 'customer'"
				 + " and c.customerId = ?1" )
	public List<Object[]> getFriendDetailById( long customerId );
	
	@Modifying
	@Query( value=" select c.customerId,"
				+ "		   c.customerName,"
				+ "		   (case when c.userLogin is null then '' else c.userLogin end),"
				+ "		   (case when c.email is null then '' else c.email end),"
				+ "		   (case when c.customerPhoneNumber is null then '' else c.customerPhoneNumber end),"
				+ "		   c.sessionToken,"
				+ "		   a.accountId,"
				+ "		   a.balance,"
				+ "		   c.customerPhoto,"
				+ "		   a.accountNumber,"
				+ "		   c.showVoucherNotification,"
				+ "		   c.showFriendRequestNotification,"
				+ "		   c.fcmToken,"
				+ "		   c.status,"
				+ "		   c.firstLogin,"
				+ "		   c.tokenExpired"
				+ " from Customer c, Account a"
				+ " where c.customerId = a.memberId "
				+ " and c.customerId = ?1"
				+ " and a.accountCategory like 'customer' " )
	public List<Object[]> getCustomerDetail( long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.fcmToken = ?1"
				+ " where c.customerId = ?2 " )
	public int updateFCMToken( String fcmToken, long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.apnToken = ?1"
				+ " where c.customerId = ?2 " )
	public int updateAPNSToken( String apnToken, long customerId );
	
	@Modifying
	@Query( value=" select a.accountId"
				+ " from Customer c, Account a"
				+ " where c.customerId = a.memberId "
				+ " and c.customerId = ?1 " )
	public List getAccountIdByCustomerId( long customerId );
	
	@Modifying
	@Query( value=" update Customer c "
				+ " set c.firstLogin = 0"
				+ " where c.customerId = ?1 " )
	public int updateFirstLogin( long customerId );
	
	@Modifying
	@Query( value = " select count(c) "
				  + " from Customer c"
				  + " where c.gmailId = ?1"
				  + " or c.fbId = ?2"
				  + " or c.twitterId = ?3" )
	public List validateMedsosId( String gmailId, String fbId, String twitterId ); 
	
	
}

