package com.webservicemaster.iirs.service.setting;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.SettingRepository;
import com.webservicemaster.iirs.domain.master.Setting;


@Service
public class SettingServiceImpl implements SettingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SettingServiceImpl.class);
	
	private SettingRepository settingRepo;
	
	@Autowired
	SettingServiceImpl( SettingRepository settingRepo ){
		this.settingRepo = settingRepo;
	}
	
	@Override
	public List<Setting> findBySettingId( String settingId ){
		return settingRepo.findBySettingId(settingId);
	}
	
	@Override
	public List<Setting> findBySettingPurpose( String settingPurpose ){
		return settingRepo.findBySettingPurpose(settingPurpose);
	}
	
	@Override
	public Setting findBySettingType( String settingType ){
		return settingRepo.findBySettingType(settingType);
	}
	
	@Override
	public Setting findBySettingPurposeAndSettingType( String settingPurpose, String settingType ){
		return settingRepo.findBySettingPurposeAndSettingType(settingPurpose, settingType);
	}
	
}
