package com.eventservice.main;

import java.util.List;

import com.eventservice.lib.model.Event;
import com.eventservice.lib.service.EventService;
import com.eventservice.lib.service.EventServiceImpl;

public class Service {

	private static EventService eventService 		= new EventServiceImpl();
	
	public static void main(String[] args) {
		
		List<Event> lEvent 		= eventService.findAll();
		
		for( Event e : lEvent ) {
			System.out.println("### Event Id : " + e.getEventId());
			System.out.println("### Event Id : " + e.getEventCode());
			System.out.println("### Event Id : " + e.getEventName());
		}

	}

}
