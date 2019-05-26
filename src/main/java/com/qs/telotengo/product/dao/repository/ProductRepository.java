package com.qs.telotengo.product.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.qs.telotengo.product.dao.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	//Todos los productos que coincidMethod has to have one of the following return types!an de un store	
	Page<Product> findByNameContainingIgnoreCaseOrTagContainingIgnoreCaseAndStatusIsTrueAndGalleryStatusIsTrue(String name, String tag, Pageable page);
	//Todos los productos de un store
	Page<Product> findByIdstoreAndStatusIsTrue(String idStore, Pageable page);
	//buscar producto por id
	Optional<Product> findByIdAndStatusIsTrue(String id);

	//Gallery method
	Optional<Product> findGalleryByIdAndStatusIsTrue(String id);
	Optional<Product> findGalleryByGalleryIdAndGalleryStatusIsTrue(String id);
	@Query(delete = true)
	void deleteGalleryByIdAndGalleryId(String idUser, String idPhoto);

	
	
	
	
}
