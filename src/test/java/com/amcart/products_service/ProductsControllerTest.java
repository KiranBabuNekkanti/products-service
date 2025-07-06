package com.amcart.product.controller;

import com.amcart.product.dto.Product;
import com.amcart.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // Success case: getProducts
    @Test
    void getProducts_success() throws Exception {
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        Mockito.when(productService.getProducts(any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.content").isArray());
    }

    // Failure case: getProducts (service throws)
    @Test
    void getProducts_serviceFailure() throws Exception {
        Mockito.when(productService.getProducts(any(), any(), any()))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/products"))
                .andExpect(status().is5xxServerError());
    }

    // Edge case: getProducts (empty result)
    @Test
    void getProducts_empty() throws Exception {
        Page<Product> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        Mockito.when(productService.getProducts(any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    // Success case: getProductDetails
    @Test
    void getProductDetails_success() throws Exception {
        Product product = new Product();
        Mockito.when(productService.getProductDetails("uuid123")).thenReturn(product);

        mockMvc.perform(get("/products/uuid123"))
                .andExpect(status().isAccepted());
    }

    // Failure case: getProductDetails (not found)
    @Test
    void getProductDetails_notFound() throws Exception {
        Mockito.when(productService.getProductDetails("bad-uuid"))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/products/bad-uuid"))
                .andExpect(status().is5xxServerError());
    }

    // Success case: addProduct
    @Test
    void addProduct_success() throws Exception {
        Product product = new Product();
        Mockito.when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isAccepted());
    }

    // Edge case: addProduct (invalid input)
    @Test
    void addProduct_invalidInput() throws Exception {
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")) // empty body
                .andExpect(status().isBadRequest());
    }

    // Add similar tests for /categories endpoint as needed
}