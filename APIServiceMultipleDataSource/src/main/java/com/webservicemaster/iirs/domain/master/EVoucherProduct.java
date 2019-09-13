package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_evoucherproducts")
public class EVoucherProduct {

	@Id
	private long voucherId;
	private long merchantId;
	private String productName;
	private String photo;
	private Double voucherValue;
	private Date startDate;
	private Date endDate;
	private short status;
	private short isAutoCampaignAfterRegister;
	private String validationPrefixUsername;
	private short isDelete;
	public long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Double getVoucherValue() {
		return voucherValue;
	}
	public void setVoucherValue(Double voucherValue) {
		this.voucherValue = voucherValue;
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
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public short getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(short isDelete) {
		this.isDelete = isDelete;
	}
	public short getIsAutoCampaignAfterRegister() {
		return isAutoCampaignAfterRegister;
	}
	public void setIsAutoCampaignAfterRegister(short isAutoCampaignAfterRegister) {
		this.isAutoCampaignAfterRegister = isAutoCampaignAfterRegister;
	}
	public String getValidationPrefixUsername() {
		return validationPrefixUsername;
	}
	public void setValidationPrefixUsername(String validationPrefixUsername) {
		this.validationPrefixUsername = validationPrefixUsername;
	}
	
	
	
}
