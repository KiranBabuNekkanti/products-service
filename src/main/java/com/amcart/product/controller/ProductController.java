package com.amcart.product.controller;

import com.amcart.product.dto.Product;
import com.amcart.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/products")
    public ResponseEntity<Page<Product>> getProducts(@PageableDefault Pageable pageable,
                                                     @RequestParam(name = "searchText", required = false) String searchText,
                                                     @RequestParam(name = "categoryIds", required = false) List<Long> categoryIds){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getProducts(pageable, searchText, categoryIds));
    }

    @GetMapping(value = "/categories", produces = {"application/json"})
    public ResponseEntity<String> getProductCategories(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getProductCategory());
    }

    @GetMapping(value = "/products/{uuid}")
    public ResponseEntity<Product> getProductDetails(@PathVariable(name = "uuid") String uuid){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.getProductDetails(uuid));
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.addProduct(product));
    }
}
