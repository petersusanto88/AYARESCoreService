package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Notification;

public interface NotificationRepository extends BaseRepository<Notification, Long>{

	void flush();
	Notification save( Notification data );
	
	List<Notification> findByHasRead( short status );
	
	@Query( value = " select notificationId, "
				+   " 		 customerId,"
				+   "		 notificationDate,"
				+ 	"		 notificationType,"
				+   "		 subject,"
				+   " 		 body,"
				+   "		 hasRead,"
				+   " 		 readAt,"
				+   "		 icon,"
				+ "			 data"
				+   " from Notification n"
				+   " where n.body like %?1%"
				+   " and n.customerId = ?2"
				+   " order by notificationDate desc" )
	List<Object[]> findNotification( String keyword, long userId, Pageable pageable);
	
	@Modifying
	@Query( " select count(n)"
		  + " from Notification n"
		  + " where customerId = ?1"
		  + " and notificationId = ?2 " )
	public List<?> isNoticationExist( long customerId, long notificationId );
	
	@Modifying
	@Query( " delete from Notification where customerId = ?1 " )
	public void deleteNotification( long id );
	
}
