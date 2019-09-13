package com.notifcation.service.main;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.notification.service.Service;
import com.notification.service.model.FCMNotification;
import com.notification.service.model.SMSEmailQueue;
import com.notification.service.util.HibernateUtil;

public class SMSEmailQueueRepository {
	
	private static String debugLabel 	= "SMSEmailQueueRepository";

	public SMSEmailQueueRepository(){}
	
	public List<SMSEmailQueue> getSMSEmailQueue(){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		String sql 						= " from SMSEmailQueue where statusSend = 0 ";
		Query q 						= session.createQuery(sql);
		List<SMSEmailQueue> lQueue		= null;
		
		try{			
			lQueue						= q.list();			
		}catch( Exception ex ){
			Service.writeDebug(" Exception<getSMSEmailQueue> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }
			
			tx.commit();
			
		}
		
		return lQueue;
		
	}
	
	public int updateStatusSend( long id ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update SMSEmailQueue set statusSend = 1 where id = :id ";
		Query q 						= session.createQuery(sql);
		q.setLong("id", id);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		
		}catch( Exception ex ){
			Service.writeDebug(" Exception<updateStatusSend> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
	public int updateStatusResponse( long id, int statusResponse, String responseMessage ){
		
		SessionFactory sessionFactory 	= HibernateUtil.getSessionFactory();
		Session session 				= sessionFactory.getCurrentSession();		
		Transaction tx 					= session.beginTransaction();
		int result 						= 0;
		
		String sql 						= " update SMSEmailQueue set statusResponse = :statusResponse,"
										+ " 						 responseMessage = :responseMessage"
										+ " where id = :id ";
		Query q 						= session.createQuery(sql);
		q.setInteger("statusResponse", statusResponse);
		q.setString("responseMessage", responseMessage);
		q.setLong("id", id);
		
		try{
			result 						= q.executeUpdate();
			tx.commit();
		}catch( Exception ex ){
			Service.writeDebug("Exception<updateStatusResponse> : " + ex.getMessage(), debugLabel);
			tx.rollback();
		}finally{
			
			if (session != null && session.isOpen()) {
				session.close();
		    }	
			
		}
		
		return result;
		
	}
	
}
