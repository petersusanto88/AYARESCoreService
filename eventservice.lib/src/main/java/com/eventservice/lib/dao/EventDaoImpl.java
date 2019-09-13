package com.eventservice.lib.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.eventservice.lib.model.Event;
import com.eventservice.lib.utility.HibernateUtil;

public class EventDaoImpl implements EventDao{

	private SessionFactory sf 			= HibernateUtil.getSessionFactory();
	private Session session				= null;
	private String debugLabel 			= "EventDaoImpl";
	
	public List<Event> findAll(){
		
		Transaction tx 					= null;
		List<Event> lEvent 				= null;
		
		try {
			
			session 					= sf.getCurrentSession();
			tx 							= session.beginTransaction();
			lEvent 						= ( List<Event> ) session.createCriteria(Event.class).list();
			
		}catch( Exception ex ) {
			System.out.println("[" + debugLabel + "] findAll : " + ex.getMessage());
		}finally {
			tx.commit();
			if( session != null && session.isOpen() ) {
				session.close();
			}
		}
		
		return lEvent;
				
	}
	
}
