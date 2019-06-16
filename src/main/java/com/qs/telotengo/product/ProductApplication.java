package com.qs.telotengo.product;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.qs.telotengo.product.dto.util.SwaggerConfiguration;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class ProductApplication {

	public static void main(String[] args) {
		TimeZone tzone = TimeZone.getTimeZone("America/Santiago");
	    tzone.setDefault(tzone);
		SpringApplication.run(ProductApplication.class, args);
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
		.addResourceLocations("classpath:/resources/");
	}

}
