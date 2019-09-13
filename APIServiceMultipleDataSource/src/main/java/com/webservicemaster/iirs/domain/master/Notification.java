package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_notifications")
public class Notification {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private long notificationId;
	
	private long customerId;
	private Date notificationDate;
	private String notificationType;
	private String subject;
	private String body;
	private short hasRead;
	private Date readAt;
	private String icon;
	private String data;
	
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
	public Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public short getHasRead() {
		return hasRead;
	}
	public void setHasRead(short hasRead) {
		this.hasRead = hasRead;
	}
	public Date getReadAt() {
		return readAt;
	}
	public void setReadAt(Date readAt) {
		this.readAt = readAt;
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
	
	
	
}
