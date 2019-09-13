package com.webservicemaster.iirs.domain.transaction;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tr_withdrawl")
public class Withdrawl implements Serializable{

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private long withdrawlId;
	
	private String postDate;
	private long accountId;
	private String accountNumber;
	private String accountName;
	private double amount;
	private double fee;
	
	private short statusTransfer;
	
	private String transferFromAccountNumber;
	private String transferFromBankName;
	private String transferFromAccountName;
	private String transferDate;
	
	private String transferToBankCode;
	private String transferToBankName;
	private String transferToAccountName;
	private String transferToAccountNumber;
	
	public long getWithdrawlId() {
		return withdrawlId;
	}
	public void setWithdrawlId(long withdrawlId) {
		this.withdrawlId = withdrawlId;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public short getStatusTransfer() {
		return statusTransfer;
	}
	public void setStatusTransfer(short statusTransfer) {
		this.statusTransfer = statusTransfer;
	}
	public String getTransferFromAccountNumber() {
		return transferFromAccountNumber;
	}
	public void setTransferFromAccountNumber(String transferFromAccountNumber) {
		this.transferFromAccountNumber = transferFromAccountNumber;
	}
	public String getTransferFromBankName() {
		return transferFromBankName;
	}
	public void setTransferFromBankName(String transferFromBankName) {
		this.transferFromBankName = transferFromBankName;
	}
	public String getTransferFromAccountName() {
		return transferFromAccountName;
	}
	public void setTransferFromAccountName(String transferFromAccountName) {
		this.transferFromAccountName = transferFromAccountName;
	}
	public String getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	public String getTransferToBankName() {
		return transferToBankName;
	}
	public void setTransferToBankName(String transferToBankName) {
		this.transferToBankName = transferToBankName;
	}
	public String getTransferToAccountName() {
		return transferToAccountName;
	}
	public void setTransferToAccountName(String transferToAccountName) {
		this.transferToAccountName = transferToAccountName;
	}
	public String getTransferToAccountNumber() {
		return transferToAccountNumber;
	}
	public void setTransferToAccountNumber(String transferToAccountNumber) {
		this.transferToAccountNumber = transferToAccountNumber;
	}
	public String getTransferToBankCode() {
		return transferToBankCode;
	}
	public void setTransferToBankCode(String transferToBankCode) {
		this.transferToBankCode = transferToBankCode;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	
}
