package com.eventservice.lib.dao;

import java.util.List;

import com.eventservice.lib.model.Event;

public interface EventDao {

	public List<Event> findAll();
	
}
