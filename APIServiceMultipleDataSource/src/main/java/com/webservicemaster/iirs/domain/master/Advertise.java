package com.webservicemaster.iirs.domain.master;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_advertises")
public class Advertise {

	@Id
	private long advertiseId;
	private String file;
	private short sequence;
	private Date startDate;
	private Date endDate;
	private short status;
	private short isDelete;
	public long getAdvertiseId() {
		return advertiseId;
	}
	public void setAdvertiseId(long advertiseId) {
		this.advertiseId = advertiseId;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public short getSequence() {
		return sequence;
	}
	public void setSequence(short sequence) {
		this.sequence = sequence;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public short getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(short isDelete) {
		this.isDelete = isDelete;
	}
	
	
	
}
