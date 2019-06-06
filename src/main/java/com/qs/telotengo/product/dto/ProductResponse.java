package com.qs.telotengo.product.dto;
import java.util.Date;
import java.util.List;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Product;
import com.qs.telotengo.product.dao.Variant;

public class ProductResponse {
	
	private String id;
	private String idstore;
	private String name;
	private String tag;
	private String details;
	private Date createDate;
	private String userCreate;
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
		this.delayTime = product.getDelayTime();
		this.variants = product.getVariants();
		this.status = product.isStatus();
	}
	@Override
	public String toString() {
		return "ProductResponse [id=" + id + ", idstore=" + idstore + ", name=" + name + ", tag=" + tag + ", details="
				+ details + ", createDate=" + createDate + ", userCreate=" + userCreate + ", type=" + type
				+ ", delayTime=" + delayTime + ", variants=" + variants + ", status=" + status + "]";
	}
	

}
