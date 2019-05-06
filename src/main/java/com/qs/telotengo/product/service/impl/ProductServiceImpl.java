package com.qs.telotengo.product.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.IterableUtils;
import org.bson.types.ObjectId;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Product;
import com.qs.telotengo.product.dao.repository.ProductRepository;
import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.PhotoResponse;
import com.qs.telotengo.product.dto.ProductRequest;
import com.qs.telotengo.product.dto.ProductResponse;
import com.qs.telotengo.product.dto.util.ProductType;
import com.qs.telotengo.product.exception.ValidationExceptions;
import com.qs.telotengo.product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());

	@Autowired
	private ProductRepository dao;

	@Autowired
	private Mapper mapper;

	@Override
	public ProductResponse saveProduct(ProductRequest productRequest) throws ValidationExceptions {
		if (!isValidType(productRequest.getType())) {
			throw new ValidationExceptions("0108", "Tipo no valido: " + productRequest.getType().toUpperCase(),
					HttpStatus.OK);
		}

		Optional<Product> productExist = dao.findByIdAndStatusIsTrue(productRequest.getId());
		Product product;
		// guardando
		if (Objects.isNull(productRequest.getId()) && !productExist.isPresent()) {
			productRequest.setStatus(true);
		    Date date = new Date();
			productRequest.setCreateDate(date);
			
			if (!Objects.isNull(productRequest.getGallery())) {
				productRequest.setGallery(setIdToPhotos(productRequest.getGallery()));
				productRequest.getGallery().get(0).setPrimary(true);
			}
			product = dao.save(buildEntity(productRequest));
			LOGGER.info("save product: " + product);
		} else {
			// Update
			productExist = dao.findByIdAndStatusIsTrue(productRequest.getId());
			if (productExist.isPresent()) {
				productRequest.setGallery(productExist.get().getGallery());
				productRequest.setIdstore(productExist.get().getIdstore());
				productRequest.setCreateDate(productExist.get().getCreateDate());
				productRequest.setUserCreate(productExist.get().getUserCreate());
				productRequest.setStatus(true);
				LOGGER.info("update product " + productExist.get());
				product = dao.save(buildEntity(productRequest));
			} else {
				throw new ValidationExceptions("0108",
						"No se puede modificar, No existe una cuenta de product para este id, Id: "
								+ productRequest.getId(),
						HttpStatus.OK);
			}
		}
		return buildResponse(product);
	}

	@Override
	public ProductResponse getProduct(String id) throws ValidationExceptions {
		Optional<Product> product = dao.findByIdAndStatusIsTrue(id);
		LOGGER.info("Message: getProduct: " + product.toString());

		ProductResponse uResponse = buildResponse(dao.findByIdAndStatusIsTrue(id).orElseThrow(
				() -> new ValidationExceptions("0101", "No existe product con este id: " + id, HttpStatus.OK)));
		return uResponse;
	}

	// busqueda producto
	@Override
	public List<ProductResponse> getAllProductCoincidencia(String coincidencia) throws ValidationExceptions {
		Iterable<Product> productList = dao
				.findByNameContainingIgnoreCaseOrTagContainingIgnoreCaseAndStatusIsTrueAndGalleryStatusIsTrue(
						coincidencia, coincidencia);
		if (IterableUtils.isEmpty(productList)) {
			throw new ValidationExceptions("4030", "La lista de products esta vacia", HttpStatus.OK);
		}
		return buildResponseList(productList);
	}

	@Override
	public List<ProductResponse> getAllProductOfStore(String idStore) throws ValidationExceptions {
		Iterable<Product> productList = dao.findByIdstoreAndStatusIsTrue(idStore);
		if (IterableUtils.isEmpty(productList)) {
			throw new ValidationExceptions("4030", "La lista de products esta vacia", HttpStatus.OK);
		}
		return buildResponseList(productList);
	}

	@Override
	public ProductResponse deleteProduct(String id) throws ValidationExceptions {
		Optional<Product> productDel = dao.findByIdAndStatusIsTrue(id);
		if (!productDel.isPresent()) {
			throw new ValidationExceptions("4031", "No existe product con este id: " + id, HttpStatus.OK);
		}
		Product product = productDel.get();
		product.setStatus(false);
		return buildResponse(dao.save(product));
	}
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||
	// |||||||||||||||GALLERY METHOD||||||||||||||||||||||||||
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@Override
	public List<Photo> getAllPhoto(String idProduct) throws ValidationExceptions {
		Optional<Product> uResponse = dao.findGalleryByIdAndStatusIsTrue(idProduct);
		if (!uResponse.isPresent()) {
			throw new ValidationExceptions("0101", "No exise product con este id: " + idProduct, HttpStatus.OK);
		} else {
			if (!Objects.isNull(uResponse.get().getGallery())) {
				return uResponse.get().getGallery();
			} else {
				throw new ValidationExceptions("0101", "Este product no tienes photos registradas", HttpStatus.OK);
			}
		}
	}

	@Override
	public List<PhotoResponse> savePhotos(List<PhotoRequest> photoList, String id) throws ValidationExceptions {
		// Photo nuevaPhoto = buildEntityGallery(photoRequest);
		Optional<Product> product = dao.findByIdAndStatusIsTrue(id);

		if (product.isPresent()) {
		LOGGER.info("Add Photos: " + product.get().getGallery());
		//buildPhotosListResponse
		return  buildPhotosListResponse(dao.save(AddPhotoInUser(product, buildPhotosListEntity(photoList)).get()).getGallery());
		} else {
			throw new ValidationExceptions("0101", "No se puese agregar fotos, no existen productos con este id: " + id, HttpStatus.OK);
		}
	}

	@Override
	public boolean deletePhoto(String idPhoto) throws ValidationExceptions {
		Optional<Product> product = dao.findGalleryByGalleryIdAndGalleryStatusIsTrue(idPhoto);
		boolean elimino = false;
		if (product.isPresent()) {
			elimino = product.get().getGallery().removeIf(l -> l.getId().equals(idPhoto));
		}
		if (elimino) {
			dao.save(product.get());
			LOGGER.info("Eliminando Gallery id: " + idPhoto);
			return elimino;
		} else {
			throw new ValidationExceptions("0101", "No se pudo eliminar la direccion No existe id: " + idPhoto,
					HttpStatus.OK);
		}
	}

	@Override
	public PhotoResponse setToPhotoPrimary(String idPhoto) throws ValidationExceptions {
		Optional<Product> productExist = dao.findGalleryByGalleryIdAndGalleryStatusIsTrue(idPhoto);
		if (!Objects.isNull(productExist)) {
			productExist.get().getGallery().stream().forEach(l -> {
				if (l.getId().equalsIgnoreCase(idPhoto)) {
					l.setPrimary(true);
				} else {
					l.setPrimary(false);
				}
			});
			dao.save(productExist.get());
			Photo gallery = productExist.get().getGallery().stream().filter(s -> s.getId().equalsIgnoreCase(idPhoto))
					.collect(Collectors.toList()).get(0);
			LOGGER.info("Se cambio esta foto a principal: " + gallery);
			return buildResponseGallery(gallery);
		} else {
			throw new ValidationExceptions("0101", "No se pudo cambiar la foto no existe id: " + idPhoto,
					HttpStatus.OK);
		}
	}

	private List<Photo> setIdToPhotos(List<Photo> lista) {
		for (int contador = 0; contador < lista.size(); contador++) {
			lista.get(contador).setId(new ObjectId().toString());
			lista.get(contador).setStatus(true);
		}
		return lista;
	}
	public boolean isValidType(String tipo) throws ValidationExceptions {
		return !Arrays.stream(ProductType.values()).filter(e -> e.equals(tipo.toUpperCase())).findFirst().isPresent();
	}

	private Optional<Product> AddPhotoInUser(Optional<Product> product, List<Photo> listPhoto) {
		List<Photo> myGallery = new ArrayList<Photo>();
		if (!Objects.isNull(product.get().getGallery())) {
			myGallery = product.get().getGallery();
		}
		
		myGallery.addAll(setIdToPhotos(listPhoto));

		if (Objects.isNull(product.get().getGallery())) {
			myGallery.get(0).setPrimary(true);
		}
		
		product.get().setGallery(myGallery);
		return product;
	}

	// UTILL Build DTO
	public ProductResponse buildResponse(Product product) {
		try {
			return mapper.map(product, ProductResponse.class);

		} catch (RuntimeException e) {

			LOGGER.info("BulidResponse" + e.getCause());
		}
		return null;
	}

	public Product buildEntity(ProductRequest productRequest) {
		try {
			return mapper.map(productRequest, Product.class);
		} catch (RuntimeException e) {

			LOGGER.info("BulidEntity: " + e.getMessage());
		}
		return null;

	}

	public List<ProductResponse> buildResponseList(Iterable<Product> productIterable) {
		List<Product> ProductRS = (List<Product>) productIterable;

		List<ProductResponse> asDto = ProductRS.stream().map(new Function<Product, ProductResponse>() {
			@Override
			public ProductResponse apply(Product s) {

				return new ProductResponse(s);
			}
		}).collect(Collectors.toList());
		return asDto;
	}

	public Photo buildEntityGallery(PhotoRequest photoRequest) {
		try {
			return mapper.map(photoRequest, Photo.class);
		} catch (RuntimeException e) {

			LOGGER.info("BulidEntityGallery: " + e.getMessage());
		}
		return null;

	}

	public PhotoResponse buildResponseGallery(Photo gallery) {
		try {
			return mapper.map(gallery, PhotoResponse.class);

		} catch (RuntimeException e) {

			LOGGER.info("BulidResponseGallery" + e.getCause());
		}
		return null;
	}
	
	
	public List<PhotoResponse> buildPhotosListResponse(Iterable<Photo> photoIterable) {
		List<Photo> PhotoRS = (List<Photo>) photoIterable;

		List<PhotoResponse> asDto = PhotoRS.stream().map(new Function<Photo, PhotoResponse>() {
			@Override
			public PhotoResponse apply(Photo s) {

				return new PhotoResponse(s);
			}
		}).collect(Collectors.toList());
		return asDto;
	}

	public List<Photo> buildPhotosListEntity(Iterable<PhotoRequest> photoRequestIterable) {
		List<PhotoRequest> PhotoRS = (List<PhotoRequest>) photoRequestIterable;

		List<Photo> asDto = PhotoRS.stream().map(new Function<PhotoRequest, Photo>() {
			@Override
			public Photo apply(PhotoRequest s) {

				return new Photo(s);
			}
		}).collect(Collectors.toList());
		return asDto;
	}



}