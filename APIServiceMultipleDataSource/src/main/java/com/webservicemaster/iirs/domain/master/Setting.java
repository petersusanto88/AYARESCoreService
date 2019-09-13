package com.webservicemaster.iirs.domain.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_settings")
public class Setting {

	@Id
	private int settingId;
	
	private String settingPurpose;
	private String settingType;
	private String value;
	public int getSettingId() {
		return settingId;
	}
	public void setSettingId(int settingId) {
		this.settingId = settingId;
	}
	public String getSettingPurpose() {
		return settingPurpose;
	}
	public void setSettingPurpose(String settingPurpose) {
		this.settingPurpose = settingPurpose;
	}
	public String getSettingType() {
		return settingType;
	}
	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
