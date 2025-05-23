package com.erp.erp.application.controller;

import com.erp.erp.application.dto.ProductDto;
import com.erp.erp.application.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/product")
@PreAuthorize("isAuthenticated()")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/brand/names")
	public ResponseEntity<?> fetchProductByBrand(@RequestParam String brand, @RequestParam(defaultValue = "0") int page) {
		Page<ProductDto> productDtoPage = productService.getByBrand(brand, page);
		if (productDtoPage.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(productDtoPage);
	}

}
