package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Setting;


public interface SettingRepository extends BaseRepository<Setting, String> {

	List<Setting> findBySettingId( String settingId );
	List<Setting> findBySettingPurpose( String settingPurpose );
	Setting findBySettingType( String settingType );
	Setting findBySettingPurposeAndSettingType( String purpose, String type );
	
}
