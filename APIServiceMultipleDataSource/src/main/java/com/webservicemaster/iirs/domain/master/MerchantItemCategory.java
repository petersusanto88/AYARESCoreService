package com.webservicemaster.iirs.domain.master;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ms_merchantitemcategories")
public class MerchantItemCategory implements Serializable {

	@Id
	private int itemCategoryId;
	private String categoryName;
	private short status;
	private short isDelete;
	private long merchantId;
	public int getItemCategoryId() {
		return itemCategoryId;
	}
	public void setItemCategoryId(int itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	
	
	
}
