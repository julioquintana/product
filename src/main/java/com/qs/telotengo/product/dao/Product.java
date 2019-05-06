package com.qs.telotengo.product.dao;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	private String tag;
	@NotNull
	private String details;
	@NotNull
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
	public void setIdstore(String idStore) {
		this.idstore = idStore;
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
	public int gettimepreparation() {
		return timepreparation;
	}
	public void settimepreparation(int  timepreparation) {
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
	public Product() {
	}
	public Product(String id, @NotNull String idstore, @NotNull String name, @NotNull String tag,
			@NotNull String details, @NotNull Timestamp createDate, @NotNull String userCreate, @NotNull String type,
			@NotNull int timepreparation, List<Photo> gallery, boolean status) {
		super();
		this.id = id;
		this.idstore = idstore;
		this.name = name;
		this.tag = tag;
		this.details = details;
		this.createDate = createDate;
		this.userCreate = userCreate;
		this.type = type;
		this.timepreparation = timepreparation;
		this.gallery = gallery;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", idstore=" + idstore + ", name=" + name + ", tag=" + tag + ", details=" + details
				+ ", createDate=" + createDate + ", userCreate=" + userCreate + ", type=" + type + ", timepreparation="
				+ timepreparation + ", gallery=" + gallery + ", status=" + status + "]";
	}

	
}
