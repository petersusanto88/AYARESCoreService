package com.webservicemaster.iirs.service.setting;

import java.util.List;

import com.webservicemaster.iirs.domain.master.Setting;

public interface SettingService {

	public List<Setting> findBySettingId( String settingId );
	public List<Setting> findBySettingPurpose( String settingPurpose );
	public Setting findBySettingType( String settingType );
	public Setting findBySettingPurposeAndSettingType( String settingPurpose, String settingType );
	
}
