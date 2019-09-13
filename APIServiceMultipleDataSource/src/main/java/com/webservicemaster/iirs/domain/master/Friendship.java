package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="ms_friendlists")
public class Friendship {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private long friendId;
	
	private Date inviteDateTime;
	private Date confirmDateTime;
	
	private long invitatorId;
	private long invitedId;
	
	private String type;
	private short status;
	private short invitationMethod;
	private short isBlockFutureInvitation;
	
	
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	public Date getInviteDateTime() {
		return inviteDateTime;
	}
	public void setInviteDateTime(Date inviteDateTime) {
		this.inviteDateTime = inviteDateTime;
	}
	
	public long getInvitatorId() {
		return invitatorId;
	}
	public void setInvitatorId(long invitatorId) {
		this.invitatorId = invitatorId;
	}
	public long getInvitedId() {
		return invitedId;
	}
	public void setInvitedId(long invitedId) {
		this.invitedId = invitedId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public short getInvitationMethod() {
		return invitationMethod;
	}
	public void setInvitationMethod(short invitationMethod) {
		this.invitationMethod = invitationMethod;
	}
	public Date getConfirmDateTime() {
		return confirmDateTime;
	}
	public void setConfirmDateTime(Date confirmDateTime) {
		this.confirmDateTime = confirmDateTime;
	}
	public short getIsBlockFutureInvitation() {
		return isBlockFutureInvitation;
	}
	public void setIsBlockFutureInvitation(short isBlockFutureInvitation) {
		this.isBlockFutureInvitation = isBlockFutureInvitation;
	}
	
	
	
}
