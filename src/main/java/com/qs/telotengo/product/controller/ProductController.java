package com.qs.telotengo.product.controller;

import java.util.List;
import java.util.Objects;
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

import com.qs.telotengo.product.dao.Photo;
import com.qs.telotengo.product.dto.PhotoRequest;
import com.qs.telotengo.product.dto.PhotoResponse;
import com.qs.telotengo.product.dto.ProductRequest;
import com.qs.telotengo.product.dto.ProductResponse;
import com.qs.telotengo.product.exception.ValidationExceptions;
import com.qs.telotengo.product.service.ProductService;
import com.qs.telotengo.product.util.Constantes;

@RestController
@RequestMapping("/product-service/v1/")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/save")
	public HttpEntity<ProductResponse> save(@Valid @RequestBody ProductRequest productRequest)
			throws ValidationExceptions {
		validateCreateRequest(productRequest);
		return new ResponseEntity<ProductResponse>(productService.saveProduct(productRequest), HttpStatus.OK);
	}

	@GetMapping("/details/{id}")
	public HttpEntity<ProductResponse> getProduct(@PathVariable("id") String id) throws ValidationExceptions {
		return new ResponseEntity<ProductResponse>(productService.getProduct(id), HttpStatus.OK);
	}

	@GetMapping("/list/{coincidencia}/{page}/{nroitem}")
	public HttpEntity<List<ProductResponse>> getAllProductCoincidencia(
			@PathVariable("coincidencia") String coincidencia, @PathVariable("page") int page, @PathVariable("nroitem") int nroitem ) throws ValidationExceptions {
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductCoincidencia(coincidencia, page,nroitem),
				HttpStatus.OK);
	}

	@GetMapping("/list/store/{idStore}/{page}/{nroitem}")
	public HttpEntity<List<ProductResponse>> getAllProductOfStore(@PathVariable("idStore") String idStore
			, @PathVariable("page") int page, @PathVariable("nroitem") int nroitem )
			throws ValidationExceptions {
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductOfStore(idStore, page,nroitem),
				HttpStatus.OK);
	}

	@PutMapping("/delete/{id}")
	public HttpEntity<ProductResponse> deleteProduct(@PathVariable("id") String id) throws ValidationExceptions {
		return new ResponseEntity<ProductResponse>(productService.deleteProduct(id), HttpStatus.OK);
	}

	// EndPoint de Gallery

	// todas las Gallery de un usuario
	@GetMapping("/list/gallery/{id}")
	public HttpEntity<List<Photo>> getAllPhoto(@PathVariable("id") String pais) throws ValidationExceptions {
		return new ResponseEntity<List<Photo>>(productService.getAllPhoto(pais), HttpStatus.OK);
	}

	// guardar Gallery de un usuario
	@PostMapping("/save/gallery/{id}")
	public HttpEntity<List<PhotoResponse>> saveGallery(@Valid @RequestBody List<PhotoRequest> photoRequest,
			@PathVariable("id") String id) throws ValidationExceptions {
		validateCreateRequestPhoto(photoRequest);
		return new ResponseEntity<List<PhotoResponse>>(productService.savePhotos(photoRequest, id), HttpStatus.OK);
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

	private void validateCreateRequest(ProductRequest request) throws ValidationExceptions {

		if (Stream.of(request.getName(), request.getIdstore(), request.getType()).anyMatch(Objects::isNull)) {
			throw new ValidationExceptions(
					Constantes.ERROR_REQUEST_NULL_DATA_CODE,
					Constantes.ERROR_REQUEST_NULL_DATA_TEXT,
					HttpStatus.BAD_REQUEST);
		}
	}
}