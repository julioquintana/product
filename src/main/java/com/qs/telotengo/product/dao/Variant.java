package com.qs.telotengo.product.dao;

import java.util.List;

import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.VariantRequest;

public class Variant {
	
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
	public Variant(VariantRequest variantRequest) {
		super();
		this.id = variantRequest.getId();
		this.colour = variantRequest.getColour();
		this.sizes = variantRequest.getSizes();
		this.gallery = variantRequest.getGallery();
	}
	public Variant(String id, String colour, List<Size> sizes, List<Photo> gallery) {
		super();
		this.id = id;
		this.colour = colour;
		this.sizes = sizes;
		this.gallery = gallery;
	}
	public Variant() {}
	@Override
	public String toString() {
		return "Variant [id=" + id + ", colour=" + colour + ", sizes=" + sizes + ", gallery=" + gallery + "]";
	}
	
	
	

}