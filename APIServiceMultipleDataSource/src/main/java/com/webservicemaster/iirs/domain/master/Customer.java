package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_customers")
public class Customer {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private long customerId;
	
	private String customerName;
	private String customerPhoto;
	private String userLogin;
	private String userPassword;
	private String email;
	private String customerPhoneNumber;
	private String registerDate;
	private short status;
	private short passwordRetry;
	private String sessionToken;
	private short isLogged;
	private String lastActivity;
	private String lastLoginVia;
	private String lastLogin;
	private Date tokenExpired; 
	private Date lastUpdate;
	private short acceptTermCondition;
	
	private short showVoucherNotification;
	private short showFriendRequestNotification;
	
	private short firstLogin;
	
	private String fcmToken;
	private String apnToken;
	
	private String registerVia;
	
	private String gmailId;
	private String fbId;
	private String twitterId;
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public short getPasswordRetry() {
		return passwordRetry;
	}
	public void setPasswordRetry(short passwordRetry) {
		this.passwordRetry = passwordRetry;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	public String getLastActivity() {
		return lastActivity;
	}
	public void setLastActivity(String lastActivity) {
		this.lastActivity = lastActivity;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public short getIsLogged() {
		return isLogged;
	}
	public void setIsLogged(short isLogged) {
		this.isLogged = isLogged;
	}
	public Date getTokenExpired() {
		return tokenExpired;
	}
	public void setTokenExpired(Date tokenExpired) {
		this.tokenExpired = tokenExpired;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getCustomerPhoto() {
		return customerPhoto;
	}
	public void setCustomerPhoto(String customerPhoto) {
		this.customerPhoto = customerPhoto;
	}
	public short getAcceptTermCondition() {
		return acceptTermCondition;
	}
	public void setAcceptTermCondition(short acceptTermCondition) {
		this.acceptTermCondition = acceptTermCondition;
	}
	public short getShowVoucherNotification() {
		return showVoucherNotification;
	}
	public void setShowVoucherNotification(short showVoucherNotification) {
		this.showVoucherNotification = showVoucherNotification;
	}
	public short getShowFriendRequestNotification() {
		return showFriendRequestNotification;
	}
	public void setShowFriendRequestNotification(short showFriendRequestNotification) {
		this.showFriendRequestNotification = showFriendRequestNotification;
	}
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}
	public short getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(short firstLogin) {
		this.firstLogin = firstLogin;
	}
	public String getApnToken() {
		return apnToken;
	}
	public void setApnToken(String apnToken) {
		this.apnToken = apnToken;
	}
	public String getRegisterVia() {
		return registerVia;
	}
	public void setRegisterVia(String registerVia) {
		this.registerVia = registerVia;
	}
	public String getLastLoginVia() {
		return lastLoginVia;
	}
	public void setLastLoginVia(String lastLoginVia) {
		this.lastLoginVia = lastLoginVia;
	}
	public String getGmailId() {
		return gmailId;
	}
	public void setGmailId(String gmailId) {
		this.gmailId = gmailId;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	
	
	
	
	
}
