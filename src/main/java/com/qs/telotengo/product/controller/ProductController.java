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

	@GetMapping("/list/{coincidencia}")
	public HttpEntity<List<ProductResponse>> getAllProductCoincidencia(
			@PathVariable("coincidencia") String coincidencia) throws ValidationExceptions {
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductCoincidencia(coincidencia),
				HttpStatus.OK);
	}

	@GetMapping("/list/store/{idStore}")
	public HttpEntity<List<ProductResponse>> getAllProductOfStore(@PathVariable("idStore") String idStore)
			throws ValidationExceptions {
		return new ResponseEntity<List<ProductResponse>>(productService.getAllProductOfStore(idStore), HttpStatus.OK);
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
	public HttpEntity<List<PhotoResponse>> saveAddresses(@Valid @RequestBody List<PhotoRequest> photoRequest,
			@PathVariable("id") String id) throws ValidationExceptions {
		validateCreateRequestPhoto(photoRequest);
		return new ResponseEntity<List<PhotoResponse>>(productService.savePhotos(photoRequest, id), HttpStatus.OK);
	}

	// borrar Gallery
	@PutMapping("/delete/photo/{id}")
	public HttpEntity<PhotoResponse> deleteAddresses(@PathVariable("id") String idPhoto) throws ValidationExceptions {
		productService.deletePhoto(idPhoto);
		return new ResponseEntity<PhotoResponse>(HttpStatus.OK);
	}

	// Cambiar a Gallery principal
	@GetMapping("/edit/photo/setprimary/{id}")
	public HttpEntity<PhotoResponse> setToAddressPrimary(@PathVariable("id") String idAPhoto)
			throws ValidationExceptions {
		return new ResponseEntity<PhotoResponse>(productService.setToPhotoPrimary(idAPhoto), HttpStatus.OK);
	}

	private void validateCreateRequestPhoto(List<PhotoRequest> request) throws ValidationExceptions {

		for (int i = 0; i < request.size(); i++) {
			if (Stream.of(request.get(i).getName()).anyMatch(Objects::isNull)) {
				throw new ValidationExceptions("4050", "Las imagenes no deben tener nombre vacio", HttpStatus.BAD_REQUEST);
			}
		}
	}

	private void validateCreateRequest(ProductRequest request) throws ValidationExceptions {

		if (Stream.of(request.getName(), request.getIdstore(), request.getType()).anyMatch(Objects::isNull)) {
			throw new ValidationExceptions("4050", "El request no debe tener datos nulos", HttpStatus.BAD_REQUEST);
		}
		// if(!isNumeric(request.getPhone()) ||
		// !cantidadDigitosValidos(request.getPhone()) ){
		// throw new ValidationExceptions("1515", "No es un numero de telefono valido",
		// HttpStatus.BAD_REQUEST);
		// }

		// && (request.getPhone().length() > 8 && request.getPhone().length() < 14)
		// && !(request.getPhone().isEmpty())

	}
}