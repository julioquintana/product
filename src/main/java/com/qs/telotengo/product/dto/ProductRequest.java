package com.qs.telotengo.product.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.qs.telotengo.product.dao.Variant;

public class ProductRequest {

	private String id;
	@NotNull
	private String idstore;
	@NotNull
	private String name;

	private String tag;
	@NotNull
	private String details;
	
	private Date createDate;
	@NotNull
	private String userCreate;
	@NotNull
	private String type;
	private int delayTime;
	private List<Variant> variants;
	private boolean status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdstore() {
		return idstore;
	}
	public void setIdstore(String idstore) {
		this.idstore = idstore;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserCreate() {
		return userCreate;
	}
	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public List<Variant> getVariants() {
		return variants;
	}
	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ProductRequest [id=" + id + ", idstore=" + idstore + ", name=" + name + ", tag=" + tag + ", details="
				+ details + ", createDate=" + createDate + ", userCreate=" + userCreate + ", type=" + type
				+ ", delayTime=" + delayTime + ", variants=" + variants + ", status=" + status + "]";
	}
	
	
}
