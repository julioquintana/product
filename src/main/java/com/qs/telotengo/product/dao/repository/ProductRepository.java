package com.qs.telotengo.product.dao.repository;

import java.util.List;
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
	
	Page<Product> findByStatusAndNameContainingIgnoreCaseOrStatusAndTagContainingIgnoreCase(String status, String name, String status2, String tag, Pageable page);
	//Todos los productos de un store
	Page<Product> findByIdstoreAndStatus(String idStore,String status, Pageable page);
	//buscar producto por id
	Optional<Product> findByIdAndStatus(String id, String status);
	Optional<Product> findByIdAndStatusNot(String id, String Status);
	Optional<Product> findByIdAndStatusAndVariantsId(String id, String status, String idVariant);

	//Variants method
	Optional<Product> findByVariantsId(String idVariant);
	Optional<Product> findByVariantsGalleryId(String id);
	//@Query(delete = true)
	//void deleteGalleryByIdAndProductVariantGalleryIdddd(String idUser, String idPhoto);
	Optional<List<Product>> findByVariantsIdIn(List<String> listaId);

}