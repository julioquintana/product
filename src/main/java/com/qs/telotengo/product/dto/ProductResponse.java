package com.qs.telotengo.product.dto;
import java.util.Date;
import java.util.List;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Product;

public class ProductResponse {
	
	private String id;
	private String idstore;
	private String name;
	private String tag;
	private String details;
	private Date createDate;
	private String userCreate;
	private String type;
	private int timePreparation;
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
	public int getTimePreparation() {
		return timePreparation;
	}
	public void setTimePreparation(int timePreparation) {
		this.timePreparation = timePreparation;
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
	public ProductResponse() {
	}
	public ProductResponse(Product product) {
		super();
		this.id =  product.getId();
		this.idstore = product.getIdstore();
		this.name = product.getName();
		this.tag = product.getTag();
		this.details = product.getDetails();
		this.createDate = product.getCreateDate();
		this.userCreate = product.getUserCreate();
		this.type = product.getType();
		this.timePreparation = product.gettimepreparation();
		this.gallery = product.getGallery();
		this.status = product.isStatus();
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", idstore=" + idstore + ", name=" + name + ", tag=" + tag + ", details=" + details
				+ ", createDate=" + createDate + ", userCreate=" + userCreate + ", type=" + type + ", timePreparation="
				+ timePreparation + ", gallery=" + gallery + ", status=" + status + "]";
	}
}
