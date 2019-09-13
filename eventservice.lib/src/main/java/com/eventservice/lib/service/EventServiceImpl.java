package com.eventservice.lib.service;

import java.util.List;

import com.eventservice.lib.dao.EventDao;
import com.eventservice.lib.dao.EventDaoImpl;
import com.eventservice.lib.model.Event;

public class EventServiceImpl implements EventService {
	
	private EventDao eventDao 	= new EventDaoImpl();

	public List<Event> findAll(){
		return eventDao.findAll();
	}
	
}
