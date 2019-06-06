package com.qs.telotengo.product.dto;

import java.util.List;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Size;

public class VariantRequest {
	
	private String id;
	private String colour;
	private List<Size> sizes;
	private List<Photo> gallery;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public List<Size> getSizes() {
		return sizes;
	}
	public void setSizes(List<Size> sizes) {
		this.sizes = sizes;
	}
	public List<Photo> getGallery() {
		return gallery;
	}
	public void setGallery(List<Photo> gallery) {
		this.gallery = gallery;
	}

}