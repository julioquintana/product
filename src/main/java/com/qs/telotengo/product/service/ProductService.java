package com.qs.telotengo.product.service;

import java.util.List;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Variant;
import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.PhotoResponse;
import com.qs.telotengo.product.dto.ProductRequest;
import com.qs.telotengo.product.dto.ProductResponse;
import com.qs.telotengo.product.dto.VariantResponse;
import com.qs.telotengo.product.exception.ValidationExceptions;

public interface ProductService {
	ProductResponse saveProduct(ProductRequest productRequest) throws ValidationExceptions;
	ProductResponse getProduct(String id) throws ValidationExceptions;
	List<ProductResponse> getAllProductCoincidencia(String coincidencia, int page, int numberOfItem) throws ValidationExceptions;
	List<ProductResponse> getAllProductOfStore(String idStore, int page, int numberOfItem)throws ValidationExceptions;
	ProductResponse deleteProduct(String id) throws ValidationExceptions; //softDelete
	
	List<PhotoResponse> getAllPhotoByVariant(String idVariant) throws ValidationExceptions;
	List<PhotoResponse> savePhotosinVariant(List<PhotoRequest> photoList, String idProduct, String idVariant) throws ValidationExceptions;
	boolean deletePhoto(String idPhoto) throws ValidationExceptions;
	PhotoResponse setToPhotoPrimary(String idPhoto) throws ValidationExceptions;
	List<Variant> saveVariant(List<Variant> variantList, String idProduct)throws ValidationExceptions;
	List<VariantResponse> getAllVariants(String idProduct)throws ValidationExceptions;
	

	
}
