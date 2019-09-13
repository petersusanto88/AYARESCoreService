package com.webservicemaster.iirs.service.smsemailqueue;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.SMSEmailRepository;
import com.webservicemaster.iirs.domain.master.SMSEmailQueue;


@Service
public class SMSEmailServiceImpl implements SMSEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SMSEmailServiceImpl.class);
	private SMSEmailRepository smsEmailRepo;
	
	@Autowired
	SMSEmailServiceImpl(SMSEmailRepository smsEmailRepo) {
        this.smsEmailRepo = smsEmailRepo;
    }
	
	@Transactional
	@Override
	public SMSEmailQueue save( SMSEmailQueue data ){
		return smsEmailRepo.save(data);
	}
	
}
