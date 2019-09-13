package com.notification.service.model;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name="tr_apnnotification" )
public class APNSNotification {

	@Id
	private long apnNotificationId;
	private long customerId;
	private Date postDate;
	private String apnToken;
	private String title;
	private String message;
	private String icon;
	private Short statusSend;
	private Short statusResponse;
	private String responseMessage;
	private String data;
	private long ayaresNotificationId ;
	public long getApnNotificationId() {
		return apnNotificationId;
	}
	public void setApnNotificationId(long apnNotificationId) {
		this.apnNotificationId = apnNotificationId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getApnToken() {
		return apnToken;
	}
	public void setApnToken(String apnToken) {
		this.apnToken = apnToken;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Short getStatusSend() {
		return statusSend;
	}
	public void setStatusSend(Short statusSend) {
		this.statusSend = statusSend;
	}
	public Short getStatusResponse() {
		return statusResponse;
	}
	public void setStatusResponse(Short statusResponse) {
		this.statusResponse = statusResponse;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getAyaresNotificationId() {
		return ayaresNotificationId;
	}
	public void setAyaresNotificationId(long ayaresNotificationId) {
		this.ayaresNotificationId = ayaresNotificationId;
	}
	
	
	
}
