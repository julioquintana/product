package com.qs.telotengo.product.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product implements Serializable {

	private static final long serialVersionUID = 40L;
	@Id
	private String id;
	@NotNull
	private String idstore;
	@NotNull
	private String name;
	@NotNull
	private String category;
	@NotNull
	private String tag;
	@NotNull
	private String details;
	@NotNull
	private Date createDate;
	@NotNull
	private String userCreate;
	@NotNull
	private String type;
	@NotNull
	private int delayTime;
	private List<Variant> variants;
	private String status;
	
	
	public Product() {}
	
	public Product(String id, @NotNull String idstore, @NotNull String name, @NotNull String category,@NotNull String tag,
			@NotNull String details, @NotNull Date createDate, @NotNull String userCreate, @NotNull String type,
			@NotNull int delayTime, List<Variant> variants, String status) {
		super();
		this.id = id;
		this.idstore = idstore;
		this.name = name;
		this.category = category;
		this.tag = tag;
		this.details = details;
		this.createDate = createDate;
		this.userCreate = userCreate;
		this.type = type;
		this.delayTime = delayTime;
		this.variants = variants;
		this.status = status;
	}
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
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", idstore=" + idstore + ", name=" + name + ", category=" + category + ", tag="
				+ tag + ", details=" + details + ", createDate=" + createDate + ", userCreate=" + userCreate + ", type="
				+ type + ", delayTime=" + delayTime + ", variants=" + variants + ", status=" + status + "]";
	}
	

	

}
