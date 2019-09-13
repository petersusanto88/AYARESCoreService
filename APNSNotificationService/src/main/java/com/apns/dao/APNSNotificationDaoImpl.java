package com.apns.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.apns.main.Service;
import com.apns.model.APNSNotification;
import com.apns.utility.HibernateUtil;

public class APNSNotificationDaoImpl implements APNSNotificationDao {
	
	public APNSNotificationDaoImpl() {}
	
	private static String debugLabel 	= "APNSNotificationDaoImpl";

	public List<APNSNotification> getAPNSNotificationList(){
		
		SessionFactory sessionFactory 		= HibernateUtil.getSessionFactory();
		Session session 					= sessionFactory.getCurrentSession();		
		Transaction tx 						= session.beginTransaction();
		List<APNSNotification> lAPNS		= null;
		
		try {
			lAPNS							= (List<APNSNotification>) session
												.createCriteria(APNSNotification.class)
												.add(Restrictions.eq("statusSend", (short)0))
												.list();
		}catch( Exception ex ) {
			Service.writeDebug("Exception <getAPNSNotificationList> : " + ex.getMessage(), debugLabel);
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
		    }
		}
		
		return lAPNS;
		
	}
	
	public void updateStatusSendAPNSNotification( long notificationId ){
		
		SessionFactory sf 				= null;
		Session session 				= null;
		Transaction tx 					= null;
		int resultUpdate 				= 0;
		
		try {
		
			sf 								= HibernateUtil.getSessionFactory();
			session 						= sf.getCurrentSession();		
			tx 								= session.beginTransaction();			
			APNSNotification updateData 	= ( APNSNotification ) session.get(APNSNotification.class, notificationId);			
			updateData.setStatusSend((short)1);
			
		}catch( Exception ex ) {			
			tx.rollback();
			Service.writeDebug("Exception <updateStatusSendAPNSNotification> : " + ex.getMessage(), debugLabel);			
		}finally {
			tx.commit();
			if (session != null && session.isOpen()) {
				session.close();
		    }
		}
		
		
	}
	
	public void updateStatusResponseAPNSNotification( long notificationId, short statusResponse, String responseMessage ) {
		
		SessionFactory sf 				= null;
		Session session 				= null;
		Transaction tx 					= null;
		int resultUpdate 				= 0;
		
		try {
		
			sf 								= HibernateUtil.getSessionFactory();
			session 						= sf.getCurrentSession();		
			tx 								= session.beginTransaction();			
			APNSNotification updateData 	= ( APNSNotification ) session.get(APNSNotification.class, notificationId);			
			updateData.setStatusResponse(statusResponse);
			updateData.setResponseMessage(responseMessage);
			
		}catch( Exception ex ) {			
			tx.rollback();
			Service.writeDebug("Exception <updateStatusResponseAPNSNotification> : " + ex.getMessage(), debugLabel);			
		}finally {
			tx.commit();
			if (session != null && session.isOpen()) {
				session.close();
		    }
		}
		
	}
	
}
