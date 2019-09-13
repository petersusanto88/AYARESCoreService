package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.APNNotification;

public interface APNNotificationRepository extends BaseRepository<APNNotification, Long> {

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
				+ " and f.status = 1 " )
	public List getTotalFriendList( long customerId );
	
}
