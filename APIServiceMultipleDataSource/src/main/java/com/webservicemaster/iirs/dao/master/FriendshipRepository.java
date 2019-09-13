package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Friendship;


public interface FriendshipRepository extends BaseRepository<Friendship, Long > {

	void flush();
	
	Friendship save( Friendship data );
	 
	public Friendship findByFriendId( long friendId );
	
	@Modifying
	@Query(" update Friendship f "
		 + " set f.status = ?1,"
		 + "	 f.isBlockFutureInvitation = ?2,"
		 + "     f.confirmDateTime = ?3"
		 + " where f.friendId = ?4 ")
	public int confirmFriendship( short status, short confirmFriendship, Date date, long friendId );
	
	@Modifying
	@Query( " select count(f) "
		  + " from Friendship f"
		  + " where ((f.invitatorId = ?1"
		  + " and f.invitedId = ?2) OR (f.invitedId = ?1"
		  + " and f.invitatorId = ?2))"
		  + " and (f.status = 1 or f.status = 0) " )
	public List checkFriendshipStatus( long invitatorId, long invitedId );
	
	@Modifying
	@Query( " select count(f) "
		  + " from Friendship f"
		  + " where (f.invitatorId = ?1"
		  + " and f.invitedId = ?2)"
		  + " and (f.status = -1) and f.isBlockFutureInvitation = 1 " )
	public List checkFriendRequestFutureBlock( long invitatorId, long invitedId );
	
	@Modifying
	@Query( " select count(f)"
		  + " from Friendship f"
		  + " where f.friendId = ?1"
		  + " and invitedId = ?2 " )
	public List checkFriendShipForConfirm( long friendId, long invitatorId );
	
	@Modifying
	@Query(" select count(f)"
		 + " from Friendship f"
		 + " where f.invitedId = ?1"
		 + " and f.type <> 'special_just_friend' ")
	public List checkIsInvitedWithSomeone( long invitedId );
	
	/*@Modifying
	@Query( " select f.friendId,"
			+ "		 c.customerName,"
			+ "		 c.customerPhoto"
		  + " from Friendship f, Customer c"
		  + " where c.customerId = f.invitedId"
		  + " and f.invitatorId = ?1"
		  + " union"
		  + " select f.friendId,"
		  + "		 c.customerName,"
		  + "		 c.customerPhoto"
		  + " from Friendship f, Customer c"
		  + " where c.customerId = f.invitatorId"
		  + " and f.invitedId = ?1 " )
	public List<Object[]> getFriendList( long customerId );*/
	
	/*@Modifying
	@Query( " select v.friendId,"
			+ "		 v.customerName,"
			+ "		 v.customerPhoto"
		  + " from ViewFriendList v "
		  + " where v.userId = ?1"
		  + " LIMIT ?2,?3 " )
	public List<Object[]> getFriendList( long customerId, int start, int limit );*/
	
}
