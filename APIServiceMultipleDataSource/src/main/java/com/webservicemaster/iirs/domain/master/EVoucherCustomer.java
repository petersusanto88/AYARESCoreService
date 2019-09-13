package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_evouchercustomers")
public class EVoucherCustomer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long voucherCustomerId;
	private long voucherId;
	private long customerId;
	private String voucherCode;
	private Date startDate;
	private Date endDate;
	private short status;
	private short isDelete;
	public long getVoucherCustomerId() {
		return voucherCustomerId;
	}
	public void setVoucherCustomerId(long voucherCustomerId) {
		this.voucherCustomerId = voucherCustomerId;
	}
	public long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getVoucherCode() {
		return voucherCode;
	}
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
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
	
	
}
