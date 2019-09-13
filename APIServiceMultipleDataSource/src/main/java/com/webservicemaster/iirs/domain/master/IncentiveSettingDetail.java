package com.webservicemaster.iirs.domain.master;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_incentivesettingdetails")
public class IncentiveSettingDetail {

	@Id
	private int incentiveSettingDetailId;
	private int incentiveSettingId;
	private String value1;
	private String value2;
	public int getIncentiveSettingDetailId() {
		return incentiveSettingDetailId;
	}
	public void setIncentiveSettingDetailId(int incentiveSettingDetailId) {
		this.incentiveSettingDetailId = incentiveSettingDetailId;
	}
	public int getIncentiveSettingId() {
		return incentiveSettingId;
	}
	public void setIncentiveSettingId(int incentiveSettingId) {
		this.incentiveSettingId = incentiveSettingId;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
	
}
