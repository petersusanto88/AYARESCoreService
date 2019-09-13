package com.notifcation.service.main;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.notification.service.Service;
import com.notification.service.model.FCMNotification;
import com.notification.service.util.HibernateUtil;

public class FCMNotificationRepository {
	
	private static String debugLabel = "FCMNotificationRepository";

	public FCMNotificationRepository(){}
	
	public List<FCMNotification> getFCMNotificationList(){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		String sql 						= " from FCMNotification where statusSend = 0 ";
		Query q 						= session.createQuery(sql);
		List<FCMNotification> lFCM		= null;
		
		try{			
			lFCM						= q.list();			
		}catch( Exception ex ){
			Service.writeDebug("Exception<getFCMNotificationList> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }
			
			tx.commit();
			
		}
		
		return lFCM;
		
	}
	
	public int updateStatusSendFCMNotification( long notificationId ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update FCMNotification set statusSend = 1 where notificationId = :id ";
		Query q 						= session.createQuery(sql);
		q.setLong("id", notificationId);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		}catch( Exception ex ){
			Service.writeDebug("Exception<updateStatusSendFCMNotification> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update FCMNotification set statusResponse = :statusResponse,"
										+ " 						   responseMessage = :responseMessage"
										+ " where notificationId = :id ";
		Query q 						= session.createQuery(sql);
		q.setInteger("statusResponse", statusResponse);
		q.setString("responseMessage", responseMessage);
		q.setLong("id", notificationId);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		}catch( Exception ex ){
			Service.writeDebug("Exception<updateStatusResponseFCMNotification> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
}
