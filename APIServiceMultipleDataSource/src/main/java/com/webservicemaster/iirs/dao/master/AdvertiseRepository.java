package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.Advertise;

public interface AdvertiseRepository extends BaseRepository<Advertise, Long> {

	void flush();
	
	@Modifying
	@Query(value=" select CONCAT(?1,file) "
			   + " from Advertise a"
			   + " where a.status = 1"
			   + " and a.isDelete = 0"
			   + " and CURRENT_DATE between startDate and endDate"
			   + " order by sequence asc ")
	public List<Advertise> getAdvertise( String advPhotoURL );
	
}
