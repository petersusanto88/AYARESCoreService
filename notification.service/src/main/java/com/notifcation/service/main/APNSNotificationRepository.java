package com.notifcation.service.main;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.notification.service.Service;
import com.notification.service.model.APNSNotification;
import com.notification.service.model.FCMNotification;
import com.notification.service.util.HibernateUtil;

public class APNSNotificationRepository {

	public APNSNotificationRepository() {}
	
	private static String debugLabel 	= "APNSNotificationRepository";
	
	public List<APNSNotification> getAPNSNotificationList(){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		String sql 						= " from APNSNotification where statusSend = 0 ";
		Query q 						= session.createQuery(sql);
		List<APNSNotification> lAPNS	= null;
		
		try{			
			lAPNS						= q.list();			
		}catch( Exception ex ){
			Service.writeDebug("Exception<getAPNSNotificationList> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }
			
			tx.commit();
			
		}
		
		return lAPNS;
		
	}
	
	
	public int updateStatusSendAPNSNotification( long notificationId ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update APNSNotification set statusSend = 1 where apnNotificationId = :id ";
		Query q 						= session.createQuery(sql);
		q.setLong("id", notificationId);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		}catch( Exception ex ){
			Service.writeDebug("Exception<updateStatusSendAPNSNotification> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
	public int updateStatusResponseAPNSNotification( long notificationId, int statusResponse, String responseMessage ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update APNSNotification set statusResponse = :statusResponse,"
										+ " 						   responseMessage = :responseMessage"
										+ " where apnNotificationId = :id ";
		Query q 						= session.createQuery(sql);
		q.setInteger("statusResponse", statusResponse);
		q.setString("responseMessage", responseMessage);
		q.setLong("id", notificationId);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		}catch( Exception ex ){
			Service.writeDebug("Exception<updateStatusResponseAPNSNotification> : " + ex.getMessage(),debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
}
