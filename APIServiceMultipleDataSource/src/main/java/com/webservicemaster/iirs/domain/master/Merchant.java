package com.webservicemaster.iirs.domain.master;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_merchants")
public class Merchant {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long merchantId;
	
	private long accountId;
	private String merchantName;
	private String logo;
	private short merchantRebateType;
	private double merchantRebateValue;
	
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public short getMerchantRebateType() {
		return merchantRebateType;
	}
	public void setMerchantRebateType(short merchantRebateType) {
		this.merchantRebateType = merchantRebateType;
	}
	public double getMerchantRebateValue() {
		return merchantRebateValue;
	}
	public void setMerchantRebateValue(double merchantRebateValue) {
		this.merchantRebateValue = merchantRebateValue;
	}
	
	
	
}
