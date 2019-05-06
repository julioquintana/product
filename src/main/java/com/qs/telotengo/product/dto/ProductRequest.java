package com.qs.telotengo.product.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.qs.telotengo.product.dao.Photo;

public class ProductRequest {

	private String id;
	@NotNull
	private String idstore;
	@NotNull
	private String name;

	private String tag;
	@NotNull
	private String details;
	
	private Timestamp createDate;
	@NotNull
	private String userCreate;
	@NotNull
	private String type;
	@NotNull
	@Min(value=0, message="No puede ser menor que 0")
	private int timepreparation;
	private List<Photo> gallery;
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
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
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
	public int getTimePreparation() {
		return timepreparation;
	}
	public void setTimePreparation(int timepreparation) {
		this.timepreparation = timepreparation;
	}
	public List<Photo> getGallery() {
		return gallery;
	}
	public void setGallery(List<Photo> gallery) {
		this.gallery = gallery;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", idstore=" + idstore + ", name=" + name + ", tag=" + tag + ", details=" + details
				+ ", createDate=" + createDate + ", userCreate=" + userCreate + ", type=" + type + ", timePreparation="
				+ timepreparation + ", gallery=" + gallery + ", status=" + status + "]";
	}
}
