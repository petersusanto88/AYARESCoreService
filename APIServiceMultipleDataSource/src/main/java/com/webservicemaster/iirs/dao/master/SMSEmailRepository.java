package com.webservicemaster.iirs.dao.master;

import com.webservicemaster.iirs.domain.master.SMSEmailQueue;

public interface SMSEmailRepository extends BaseRepository<SMSEmailQueue, Long> {

	SMSEmailQueue save( SMSEmailQueue data );
	
}
