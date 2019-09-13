package com.webservicemaster.iirs.domain.transaction;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tr_transactiondetails")
public class TransactionDetail implements Serializable {

	@Id
	private Long transDetailId;
	private Long transId;
	private Long transactionCodeId;
	private String transactionName;
	private Long accountIdDestination;
	private String accountNumberDestination;
	private String transType;
	private Double total;
	private Date transactionDate;
	
	public Long getTransDetailId() {
		return transDetailId;
	}
	public void setTransDetailId(Long transDetailId) {
		this.transDetailId = transDetailId;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}
	public void setTransactionCodeId(Long transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}
	public String getTransactionName() {
		return transactionName;
	}
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}
	public Long getAccountIdDestination() {
		return accountIdDestination;
	}
	public void setAccountIdDestination(Long accountIdDestination) {
		this.accountIdDestination = accountIdDestination;
	}
	public String getAccountNumberDestination() {
		return accountNumberDestination;
	}
	public void setAccountNumberDestination(String accountNumberDestination) {
		this.accountNumberDestination = accountNumberDestination;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
