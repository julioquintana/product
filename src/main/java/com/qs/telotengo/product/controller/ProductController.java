package com.qs.telotengo.product.controller;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qs.telotengo.product.dao.Variant;
import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.PhotoResponse;
import com.qs.telotengo.product.dto.ProductRequest;
import com.qs.telotengo.product.dto.ProductResponse;
import com.qs.telotengo.product.dto.VariantResponse;
import com.qs.telotengo.product.dto.util.StatusType;
import com.qs.telotengo.product.exception.ValidationExceptions;
import com.qs.telotengo.product.service.ProductService;
import com.qs.telotengo.product.util.Constantes;

@RestController
@RequestMapping("/v1/product-service/")
public class ProductController {
	
	private final Logger LOGGER = Logger.getLogger(ProductController.class.getName());


	@Autowired
	private ProductService productService;

	@PostMapping("/save")
	public HttpEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest)
			throws ValidationExceptions {
		validateCreateRequest(productRequest);
		return new ResponseEntity<ProductResponse>(productService.saveProduct(productRequest), HttpStatus.OK);
	}

	@GetMapping("/details/{status}/{id}")
	public HttpEntity<ProductResponse> getProduct(@PathVariable("status") String status,@PathVariable("id") String id) throws ValidationExceptions {
		status = fixStatusRequest(status);
		return new ResponseEntity<ProductResponse>(productService.getProductByIdAndStatus(id, status), HttpStatus.OK);
	}

	@GetMapping("/list/{status}/{coincidencia}/{page}/{nroitem}")
	public HttpEntity<List<ProductResponse>> getAllProductCoincidencia(@PathVariable("status") String status,
			@PathVariable("coincidencia") String coincidencia, @PathVariable("page") int page, @PathVariable("nroitem") int nroitem ) throws ValidationExceptions {
		status = fixStatusRequest(status);
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductCoincidencia(coincidencia,status, page,nroitem),
				HttpStatus.OK);
	}

	@GetMapping("/list/store/{status}/{idStore}/{page}/{nroitem}")
	public HttpEntity<List<ProductResponse>> getAllProductOfStore(@PathVariable("status") String status, @PathVariable("idStore") String idStore
			, @PathVariable("page") int page, @PathVariable("nroitem") int nroitem )throws ValidationExceptions {
		status = fixStatusRequest(status);
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductOfStore(idStore, status, page,nroitem),
				HttpStatus.OK);
	}

	@PutMapping("/delete/{id}")
	public HttpEntity<ProductResponse> deleteProduct(@PathVariable("id") String id) throws ValidationExceptions {
		return new ResponseEntity<ProductResponse>(productService.deleteProduct(id), HttpStatus.OK);
	}

	// EndPoint de Gallery

	// todas las Gallery de una variante
	@GetMapping("/list/gallery/variant/{idVariant}")
	public HttpEntity<List<PhotoResponse>> getAllPhotoByVariant(@PathVariable("idVariant") String idVariant) throws ValidationExceptions {
		return new ResponseEntity<List<PhotoResponse>>(productService.getAllPhotoByVariant(idVariant), HttpStatus.OK);
	}

	// guardar Gallery de una variante de un producto
	@PostMapping("/save/gallery/{id}/{idVariant}")
	public HttpEntity<List<PhotoResponse>> saveGallery(@Valid @RequestBody List<PhotoRequest> photoRequest,
			@PathVariable("id") String id,@PathVariable("idVariant") String idVariant ) throws ValidationExceptions {
		validateCreateRequestPhoto(photoRequest);
		return new ResponseEntity<List<PhotoResponse>>(productService.savePhotosinVariant(photoRequest, id, idVariant), HttpStatus.OK);
	}

	// borrar Gallery
	@PutMapping("/delete/photo/{id}")
	public HttpEntity<PhotoResponse> deletePhoto(@PathVariable("id") String idPhoto) throws ValidationExceptions {
		productService.deletePhoto(idPhoto);
		return new ResponseEntity<PhotoResponse>(HttpStatus.OK);
	}

	// Cambiar a Gallery principal
	@GetMapping("/edit/photo/setprimary/{id}")
	public HttpEntity<PhotoResponse> setToPhotoPrimary(@PathVariable("id") String idAPhoto)
			throws ValidationExceptions {
		return new ResponseEntity<PhotoResponse>(productService.setToPhotoPrimary(idAPhoto), HttpStatus.OK);
	}

	// todas las Variantes de un producto
	@GetMapping("/list/variants/{idProducto}")
	public HttpEntity<List<VariantResponse>> getAllVariants(@PathVariable("idProducto") String idProducto) throws ValidationExceptions {
		return new ResponseEntity<List<VariantResponse>>(productService.getAllVariants(idProducto), HttpStatus.OK);
	}
	
	/*
	 * 
	 * 
	 * EndPoint Variants
	 */
	
	// guardar Variants de un producto
	@PostMapping("/save/variants/{idProduct}")
	public HttpEntity<List<Variant>> saveVariants(@Valid @RequestBody List<Variant> variantRequest,
			@PathVariable("idProduct") String idProduct) throws ValidationExceptions {
		return new ResponseEntity<List<Variant>>(productService.saveVariant(variantRequest,idProduct), HttpStatus.OK);
	}

	private void validateCreateRequestPhoto(List<PhotoRequest> request) throws ValidationExceptions {

		for (int i = 0; i < request.size(); i++) {
			if (Stream.of(request.get(i).getName()).anyMatch(Objects::isNull)) {
				throw new ValidationExceptions(
						Constantes.ERROR_IMAGE_NAME_NULL_CODE,
						Constantes.ERROR_IMAGE_NAME_NULL_TEXT,
						HttpStatus.BAD_REQUEST);
			}
		}
	}
	private String fixStatusRequest(String status) throws ValidationExceptions {
		try {
			status = status.toUpperCase();
			return StatusType.valueOf(status).toString();
		}catch(Exception e){
			LOGGER.info(Constantes.ERROR_STATUS_PRODUCT_NO_VALID_CODE);
			throw new ValidationExceptions(
					Constantes.ERROR_STATUS_PRODUCT_NO_VALID_CODE,
					Constantes.ERROR_STATUS_PRODUCT_NO_VALID_TEXT,
					HttpStatus.BAD_REQUEST);
		}
	}

	private void validateCreateRequest(ProductRequest request) throws ValidationExceptions {

		if (Stream.of(request.getName(), request.getIdstore(), request.getType()).anyMatch(Objects::isNull)) {
			throw new ValidationExceptions(
					Constantes.ERROR_REQUEST_NULL_DATA_CODE,
					Constantes.ERROR_REQUEST_NULL_DATA_TEXT,
					HttpStatus.BAD_REQUEST);
		}
	}
}