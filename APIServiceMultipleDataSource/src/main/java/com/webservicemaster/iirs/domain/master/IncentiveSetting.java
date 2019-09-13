package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_incentivesettings")
public class IncentiveSetting {

	@Id
	private int incentiveSettingId;
	
	private String settingName;
	private String incentiveType;
	private Date startDate;
	private Date endDate;
	private Short status;
	public int getIncentiveSettingId() {
		return incentiveSettingId;
	}
	public void setIncentiveId(int incentiveSettingId) {
		this.incentiveSettingId = incentiveSettingId;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public String getIncentiveType() {
		return incentiveType;
	}
	public void setIncentiveType(String incentiveType) {
		this.incentiveType = incentiveType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	
	
}
