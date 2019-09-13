package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tr_fcmnotification")
public class FCMNotification {

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY )
	private long notificationId;
	private long customerId;
	private Date postDate;
	private String fcmToken;
	private String title;
	private String message;
	private String icon;
	private Short statusSend;
	private Short statusResponse;
	private String responseMessage;
	private String data;
	private long ayaresNotificationId;
	
	public long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
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
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
