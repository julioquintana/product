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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dao.Product;
import com.qs.telotengo.product.dao.Size;
import com.qs.telotengo.product.dao.Variant;
import com.qs.telotengo.product.dao.repository.ProductRepository;
import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.PhotoResponse;
import com.qs.telotengo.product.dto.ProductRequest;
import com.qs.telotengo.product.dto.ProductResponse;
import com.qs.telotengo.product.dto.VariantRequest;
import com.qs.telotengo.product.dto.VariantResponse;
import com.qs.telotengo.product.dto.util.ProductType;
import com.qs.telotengo.product.exception.ValidationExceptions;
import com.qs.telotengo.product.service.ProductService;
import com.qs.telotengo.product.util.Constantes;

@Service
public class ProductServiceImpl implements ProductService {

	private final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());

	@Autowired
	private ProductRepository dao;

	@Autowired
	private Mapper mapper;

	@Override
	public ProductResponse saveProduct(ProductRequest productRequest) throws ValidationExceptions {
		isValidType(productRequest.getType());

		Optional<Product> productExist = dao.findByIdAndStatusIsTrue(productRequest.getId());

		Product product;
		// guardando
		if (Objects.isNull(productRequest.getId()) && !productExist.isPresent()) {

			productRequest.setStatus(true);
			productRequest.setCreateDate(new Date());
			productRequest.setId(new ObjectId().toString());
			productRequest.setVariants(null);

			product = dao.save(buildEntity(productRequest));
			LOGGER.info("save product: " + product);
		} else if (productExist.isPresent()) {
			// Update

			productRequest.setVariants(productExist.get().getVariants());
			productRequest.setIdstore(productExist.get().getIdstore());
			productRequest.setCreateDate(productExist.get().getCreateDate());
			productRequest.setUserCreate(productExist.get().getUserCreate());
			productRequest.setStatus(true);
			product = dao.save(buildEntity(productRequest));
			LOGGER.info("update product " + productRequest);
		} else {
			LOGGER.info(Constantes.ERROR_NOT_UPDATE_PRODUCT_NOT_EXIST_TEXT);
			throw new ValidationExceptions(Constantes.ERROR_NOT_UPDATE_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_NOT_UPDATE_PRODUCT_NOT_EXIST_TEXT, HttpStatus.OK);
		}
		return buildResponse(product);
	}

	private boolean variantIdConsistency(List<String> listaRequestVariantIdExist) {
		Optional<List<Product>> listVariantsExist = dao.findByVariantsIdIn(listaRequestVariantIdExist);
		if (listVariantsExist.get().size() != 1) {
			return false;
		}
		if (listVariantsExist.get().get(0).getVariants().size() != listaRequestVariantIdExist.size()) {
			return false;
		}

		return true;
	}

	@Override
	public ProductResponse getProduct(String id) throws ValidationExceptions {
		Optional<Product> product = dao.findByIdAndStatusIsTrue(id);
		LOGGER.info("Message: getProduct: " + product.toString());

		ProductResponse uResponse = buildResponse(dao.findByIdAndStatusIsTrue(id)
				.orElseThrow(() -> new ValidationExceptions(Constantes.ERROR_PRODUCT_NOT_EXIST_CODE,
						Constantes.ERROR_PRODUCT_NOT_EXIST_TEXT + id, HttpStatus.OK)));
		return uResponse;
	}

	// busqueda producto
	@Override
	public List<ProductResponse> getAllProductCoincidencia(String coincidencia, int page, int numberOfItem)
			throws ValidationExceptions {
		PageRequest pageable = PageRequest.of(page, numberOfItem);
		List<Product> productList = dao.findByNameContainingIgnoreCaseOrTagContainingIgnoreCaseAndStatusIsTrue(
				coincidencia, coincidencia, pageable).getContent();
		if (IterableUtils.isEmpty(productList)) {
			throw new ValidationExceptions(Constantes.ERROR_EMPTY_PRODUCT_LIST_CODE,
					Constantes.ERROR_EMPTY_PRODUCT_LIST_TEXT, HttpStatus.OK);
		}
		return buildResponseList(productList);
	}

	@Override
	public List<ProductResponse> getAllProductOfStore(String idStore, int page, int numberOfItem)
			throws ValidationExceptions {
		PageRequest pageable = PageRequest.of(page, numberOfItem);
		List<Product> productList = dao.findByIdstoreAndStatusIsTrue(idStore, pageable).getContent();
		if (IterableUtils.isEmpty(productList)) {
			throw new ValidationExceptions(Constantes.ERROR_EMPTY_PRODUCT_LIST_CODE,
					Constantes.ERROR_EMPTY_PRODUCT_LIST_TEXT, HttpStatus.OK);
		}
		return buildResponseList(productList);
	}

	@Override
	public ProductResponse deleteProduct(String id) throws ValidationExceptions {
		Optional<Product> productDel = dao.findByIdAndStatusIsTrue(id);
		if (!productDel.isPresent()) {
			throw new ValidationExceptions(Constantes.ERROR_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_PRODUCT_NOT_EXIST_TEXT + id, HttpStatus.OK);
		}
		Product product = productDel.get();
		product.setStatus(false);
		return buildResponse(dao.save(product));
	}
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	// |||||||||||||||||||||GALLERY VARIANTS||||||||||||||||||||||||
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

	@Override
	public List<Variant> saveVariant(List<Variant> variantList, String idProduct) throws ValidationExceptions {
		Optional<Product> product = dao.findByIdAndStatusIsTrue(idProduct);

		if (product.isPresent()) {

			List<String> listaRequestVariantIdExist = (List<String>) variantList.stream()
					.filter(s -> !Objects.isNull(s.getId()))
					.map(Variant::getId)
					.collect(Collectors.toList());

			//Optional<List<Product>> listProductWithVariants = dao.findByVariantsIdIn(listaRequestVariantIdExist);

			
			if (listaRequestVariantIdExist.size() > 0 && !variantIdConsistency(listaRequestVariantIdExist)) {
				LOGGER.info("Error Add Variants: " + Constantes.ERROR_INCONSISTENCY_VARIANT_TEXT  +" - "+variantList);
				throw new ValidationExceptions(Constantes.ERROR_INCONSISTENCY_VARIANT_CODE,
						Constantes.ERROR_INCONSISTENCY_VARIANT_TEXT + ": " + Arrays.toString(variantList.toArray()), HttpStatus.BAD_REQUEST);
			}
			variantList = setIdToVariantWithoutId(variantList);
			// set id to gallery
			for (int index = 0; index < variantList.size(); index++) {
				variantList.get(index).setGallery(setIdToPhotos(variantList.get(index).getGallery()));
				variantList.get(index).getGallery().get(0).setPrimary(true);
				variantList.get(index).setSizes(setIdToSizes(variantList.get(index).getSizes()));
			}

			product.get().setVariants(variantList);

			dao.save(product.get());

			LOGGER.info("Add this Variants: " + variantList);
			// buildPhotosListResponse
			return variantList;
		} else {
			throw new ValidationExceptions(Constantes.ERROR_NOT_ADD_PHOTOS_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_NOT_ADD_PHOTOS_PRODUCT_NOT_EXIST_TEXT + " :" + idProduct, HttpStatus.OK);
		}
	}

	@Override
	public List<VariantResponse> getAllVariants(String idProduct) throws ValidationExceptions {
		Optional<Product> product = dao.findByIdAndStatusIsTrue(idProduct);

		if (product.isPresent() && !Objects.isNull(product.get().getVariants())) {
			LOGGER.info("Get list Variants: " + product.get().getVariants().toString());
			return  buildVariantsListResponse(product.get().getVariants());
		} else if (product.isPresent() && Objects.isNull(product.get().getVariants())) {
			LOGGER.info("ERROR Get list Variants: " + Constantes.ERROR_EMPTY_LIST_VARIANT_TEXT);
			throw new ValidationExceptions(Constantes.ERROR_EMPTY_LIST_VARIANT_CODE,
					Constantes.ERROR_EMPTY_LIST_VARIANT_TEXT + " :" + idProduct, HttpStatus.OK);
		}else {
			LOGGER.info("ERROR Get list Variants: " + Constantes.ERROR_PRODUCT_NOT_EXIST_TEXT);
			throw new ValidationExceptions(Constantes.ERROR_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_PRODUCT_NOT_EXIST_TEXT + " :" + idProduct, HttpStatus.OK);			
		}
	}

	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	// |||||||||||||||||||||GALLERY METHOD||||||||||||||||||||||||||
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	@Override
	public List<PhotoResponse> getAllPhotoByVariant(String idVariant) throws ValidationExceptions {
		Optional<Product> uResponse = dao.findByVariantsId(idVariant);
		if (!uResponse.isPresent()) {
			throw new ValidationExceptions(Constantes.ERROR_VARIANT_NOT_EXIST_CODE,
					Constantes.ERROR_VARIANT_NOT_EXIST_TEXT + " :" + idVariant, HttpStatus.OK);
		} else {
			if (!Objects.isNull(uResponse.get().getVariants().get(0).getGallery())) {
				return buildPhotosListResponse(uResponse.get().getVariants().get(0).getGallery());
			} else {
				throw new ValidationExceptions(Constantes.ERROR_EMPTY_PHOTOS_LIST_CODE,
						Constantes.ERROR_EMPTY_PHOTOS_LIST_TEXT, HttpStatus.OK);
			}
		}
	}

	@Override
	public List<PhotoResponse> savePhotosinVariant(List<PhotoRequest> photoList, String idProduct, String idVariant)
			throws ValidationExceptions {
		// Photo nuevaPhoto = buildEntityGallery(photoRequest);
		Optional<Product> product = dao.findByIdAndVariantsIdAndStatusIsTrue(idProduct, idVariant);

		if (product.isPresent()) {
			LOGGER.info("Add Photos: " + photoList);
			// buildPhotosListResponse
			return buildPhotosListResponse(dao.save(AddPhotoInVariant(product, buildPhotosListEntity(photoList)).get())
					.getVariants().get(0).getGallery());
		} else {
			throw new ValidationExceptions(Constantes.ERROR_NOT_ADD_PHOTOS_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_NOT_ADD_PHOTOS_PRODUCT_NOT_EXIST_TEXT + " :" + idProduct, HttpStatus.OK);
		}
	}

	@Override
	public boolean deletePhoto(String idPhoto) throws ValidationExceptions {
		Optional<Product> product = dao.findByVariantsGalleryId(idPhoto);
		boolean elimino = false;
		if (product.isPresent()) {
			elimino = product.get().getVariants().get(0).getGallery().removeIf(l -> l.getId().equals(idPhoto));
		}
		if (elimino) {
			dao.save(product.get());
			LOGGER.info("Eliminando foto id: " + idPhoto);
			return elimino;
		} else {
			throw new ValidationExceptions(Constantes.ERROR_NOT_DELETE_PHOTOS_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_NOT_DELETE_PHOTOS_PRODUCT_NOT_EXIST_TEXT + " :" + idPhoto, HttpStatus.OK);
		}
	}

	@Override
	public PhotoResponse setToPhotoPrimary(String idPhoto) throws ValidationExceptions {
		Optional<Product> productExist = dao.findByVariantsGalleryId(idPhoto);
		if (!Objects.isNull(productExist)) {
			productExist.get().getVariants().get(0).getGallery().stream().forEach(l -> {
				if (l.getId().equalsIgnoreCase(idPhoto)) {
					l.setPrimary(true);
				} else {
					l.setPrimary(false);
				}
			});
			dao.save(productExist.get());
			Photo gallery = productExist.get().getVariants().get(0).getGallery().stream()
					.filter(s -> s.getId().equalsIgnoreCase(idPhoto)).collect(Collectors.toList()).get(0);
			LOGGER.info("Se cambio esta foto a principal: " + gallery);
			return buildResponseGallery(gallery);
		} else {
			throw new ValidationExceptions(Constantes.ERROR_NOT_UPDATE_PHOTOS_PRODUCT_NOT_EXIST_CODE,
					Constantes.ERROR_NOT_UPDATE_PHOTOS_PRODUCT_NOT_EXIST_TEXT + " :" + idPhoto, HttpStatus.OK);
		}
	}

	private List<Size> setIdToSizes(List<Size> lista) {
		for (int contador = 0; contador < lista.size(); contador++) {
			if (Objects.isNull(lista.get(contador).getId()) || lista.get(contador).getId().isEmpty()) {
				lista.get(contador).setId(new ObjectId().toString());
			}
		}
		return lista;
	}

	private List<Photo> setIdToPhotos(List<Photo> lista) {
		for (int contador = 0; contador < lista.size(); contador++) {
			if (Objects.isNull(lista.get(contador).getId()) || lista.get(contador).getId().isEmpty()) {
				lista.get(contador).setId(new ObjectId().toString());
				lista.get(contador).setStatus(true);
			}
		}
		return lista;
	}

	public void isValidType(String tipo) throws ValidationExceptions {
		// boolean respuesta = Arrays.stream(ProductType.values()).filter(e ->
		// e.equals(tipo.toUpperCase())).findFirst().isPresent();
		boolean result = false;
		for (ProductType type : ProductType.values()) {
			if (type.toString().equalsIgnoreCase(tipo)) {
				result = true;
				break;
			}
		}

		if (!result) {

			throw new ValidationExceptions(Constantes.ERROR_TYPE_PRODUCT_NO_VALID_CODE,
					Constantes.ERROR_TYPE_PRODUCT_NO_VALID_TEXT + " :" + tipo.toUpperCase(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * Debe enviar un solo variante
	 */
	private Optional<Product> AddPhotoInVariant(Optional<Product> product, List<Photo> listPhoto)
			throws ValidationExceptions {
		if (product.get().getVariants().size() > 1) {
			throw new ValidationExceptions(Constantes.ERROR_UNSPECIFIED_VARIANT_CODE,
					Constantes.ERROR_UNSPECIFIED_VARIANT_TEXT, HttpStatus.OK);
		}
		List<Photo> myGallery = new ArrayList<Photo>();
		if (!Objects.isNull(product.get().getVariants().get(0).getGallery())) {
			myGallery = product.get().getVariants().get(0).getGallery();
		}

		myGallery.addAll(setIdToPhotos(listPhoto));

		if (Objects.isNull(product.get().getVariants().get(0).getGallery())) {
			myGallery.get(0).setPrimary(true);
		}

		product.get().getVariants().get(0).setGallery(myGallery);
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

	public Variant buildEntityVariant(VariantRequest variantRequest) {
		try {
			return mapper.map(variantRequest, Variant.class);
		} catch (RuntimeException e) {

			LOGGER.info("BulidEntityVariant: " + e.getMessage());
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

	public List<Variant> buildVariantListEntity(Iterable<VariantRequest> VariantRequestIterable) {
		List<VariantRequest> PhotoRS = (List<VariantRequest>) VariantRequestIterable;

		List<Variant> asDto = PhotoRS.stream().map(new Function<VariantRequest, Variant>() {
			@Override
			public Variant apply(VariantRequest s) {

				return new Variant(s);
			}
		}).collect(Collectors.toList());
		return asDto;
	}
	
	public List<VariantResponse> buildVariantsListResponse(Iterable<Variant> variantIterable) {
		List<Variant> PhotoRS = (List<Variant>) variantIterable;

		List<VariantResponse> asDto = PhotoRS.stream().map(new Function<Variant, VariantResponse>() {
			@Override
			public VariantResponse apply(Variant s) {

				return new VariantResponse(s);
			}
		}).collect(Collectors.toList());
		return asDto;
	}

	private List<Variant> setIdToVariantWithoutId(List<Variant> lista) {
		for (int contador = 0; contador < lista.size(); contador++) {
			if (Objects.isNull(lista.get(contador).getId()) || lista.get(contador).getId().isEmpty())
				lista.get(contador).setId(new ObjectId().toString());
		}
		return lista;
	}

}