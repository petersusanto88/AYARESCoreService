package com.webservicemaster.iirs.dao.master;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.webservicemaster.iirs.domain.master.EVoucherProduct;

public interface EVoucherRepository extends BaseRepository<EVoucherProduct, Long> {

	@Modifying
	@Query( value = " select e.voucherId,"
				  + "	     e.merchantId,"
				  + "		 e.voucherValue,"
				  + "		 e.validationPrefixUsername"
				  + " from EVoucherProduct e"
				  + " where ? between e.startDate and e.endDate"
				  + " and e.status = 1"
				  + " and e.isDelete = 0 "
				  + " and e.isAutoCampaignAfterRegister = ?"
				  + " and e. validationPrefixUsername like ?" )
	public List<Object[]> getEVoucherAutoCampaign( Date date, Short isAutoCampaignAfterRegister, String validationPrefixUsername );
	
	
}
