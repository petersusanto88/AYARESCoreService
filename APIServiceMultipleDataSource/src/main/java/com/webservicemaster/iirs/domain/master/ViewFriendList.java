package com.webservicemaster.iirs.domain.master;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="v_friendlist")
public class ViewFriendList implements Serializable {

	@Id
	private long friendId;
	
	@Id
	private long userId;
	
	private long customerId;
	private String customerName;
	private String customerPhoto;
	private String type;
	
	private short isInvitator;
	
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhoto() {
		return customerPhoto;
	}
	public void setCustomerPhoto(String customerPhoto) {
		this.customerPhoto = customerPhoto;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public short getIsInvitator() {
		return isInvitator;
	}
	public void setIsInvitator(short isInvitator) {
		this.isInvitator = isInvitator;
	}
	
}
