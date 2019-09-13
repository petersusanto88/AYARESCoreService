package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.IncentiveSetting;

public interface IncentiveSettingRepository extends BaseRepository<IncentiveSetting, Integer> {

	@Modifying
	@Query( value = " select i.settingName,"
				  + " 		 i.startDate,"
				  + "		 i.endDate,"
				  + "		 isd.value1,"
				  + "		 isd.value2"
				  + " from IncentiveSetting i, IncentiveSettingDetail isd"
				  + " where i.incentiveSettingId = isd.incentiveSettingId"
				  + " and i.incentiveType like ?1"
				  + " and i.status = 1"
				  + " and ( i.startDate <= ?2 and i.endDate >= ?2 ) " )
	public List<Object[]> getIncentiveSettingByIncentiveType( String type, Date now );
	
}
