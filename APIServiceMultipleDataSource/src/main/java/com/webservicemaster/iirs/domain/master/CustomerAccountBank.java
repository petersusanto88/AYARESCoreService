package com.webservicemaster.iirs.domain.master;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_customeraccountbank")
public class CustomerAccountBank {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long accountBankId;
	
	private long customerId;
	private String bankCode;
	private String bankName;
	private String bankAccountName;
	private String bankAccountNumber;
	private String identityCard;
	private String fileIdentity;
	public long getAccountBankId() {
		return accountBankId;
	}
	public void setAccountBankId(long accountBankId) {
		this.accountBankId = accountBankId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getFileIdentity() {
		return fileIdentity;
	}
	public void setFileIdentity(String fileIdentity) {
		this.fileIdentity = fileIdentity;
	}
	
	
	
}
