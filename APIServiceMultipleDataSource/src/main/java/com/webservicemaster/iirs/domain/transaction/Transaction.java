package com.webservicemaster.iirs.domain.transaction;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.Table;

@Entity
@Table(name="tr_transactions")
public class Transaction implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long transactionId;
	
	private String transactionNumber;
	private String merchantTransactionNumber;
	private long accountId;
	private long voucherId;
	private String accountNumber;
	private String transDate;
	private Long totalPurchase;
	private Long totalRebate;
	private short rebateType;
	private short transactionType;
	private short status;
	private Long merchantId;
	private String merchantName;
	private Long branchId;
	private String branchName;
	private String merchantIcon40x60;
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getMerchantTransactionNumber() {
		return merchantTransactionNumber;
	}
	public void setMerchantTransactionNumber(String merchantTransactionNumber) {
		this.merchantTransactionNumber = merchantTransactionNumber;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(long voucherId) {
		this.voucherId = voucherId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public Long getTotalPurchase() {
		return totalPurchase;
	}
	public void setTotalPurchase(Long totalPurchase) {
		this.totalPurchase = totalPurchase;
	}
	public Long getTotalRebate() {
		return totalRebate;
	}
	public void setTotalRebate(Long totalRebate) {
		this.totalRebate = totalRebate;
	}
	public short getRebateType() {
		return rebateType;
	}
	public void setRebateType(short rebateType) {
		this.rebateType = rebateType;
	}
	public short getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(short transactionType) {
		this.transactionType = transactionType;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getMerchantIcon40x60() {
		return merchantIcon40x60;
	}
	public void setMerchantIcon40x60(String merchantIcon40x60) {
		this.merchantIcon40x60 = merchantIcon40x60;
	}
	
	
	
}
