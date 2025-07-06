package com.amcart.product.service;

import com.amcart.product.dto.Product;
import com.amcart.product.dto.CategoryHierarchyView;
import com.amcart.product.model.CategoryHierarchy;
import com.amcart.product.repository.CategoryRepository;
import com.amcart.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // getProducts tests
    @Test
    void getProducts_success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(new Product());
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(products));
        Page<Product> result = productService.getProducts(pageable, null, null);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getProducts_withFilters_success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> categoryIds = List.of(1L);
        Set<Long> subCategoryIds = new HashSet<>(List.of(2L));
        when(categoryRepository.getSubCategoryIds(categoryIds)).thenReturn(subCategoryIds);
        when(productRepository.fetchProductsUsingSearchTextAndCategories(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(new Product())));
        Page<Product> result = productService.getProducts(pageable, "test", categoryIds);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getProducts_nullPageable_throws() {
        assertThrows(IllegalArgumentException.class, () -> productService.getProducts(null, null, null));
    }

    @Test
    void getProducts_searchTextTooLong_throws() {
        Pageable pageable = PageRequest.of(0, 10);
        String longText = "a".repeat(256);
        assertThrows(IllegalArgumentException.class, () -> productService.getProducts(pageable, longText, null));
    }

    @Test
    void getProducts_tooManyCategoryIds_throws() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 101; i++) ids.add((long) i);
        assertThrows(IllegalArgumentException.class, () -> productService.getProducts(pageable, null, ids));
    }

    // getProductDetails tests
    @Test
    void getProductDetails_success() {
        UUID uuid = UUID.randomUUID();
        Product product = new Product();
        product.setCategoryId(1L);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        Product result = productService.getProductDetails(uuid.toString());
        assertNotNull(result);
    }

    @Test
    void getProductDetails_invalidUUID_throws() {
        assertThrows(NoSuchElementException.class, () -> productService.getProductDetails("bad-uuid"));
    }

    @Test
    void getProductDetails_notFound_throws() {
        UUID uuid = UUID.randomUUID();
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> productService.getProductDetails(uuid.toString()));
    }

    @Test
    void getProductDetails_nullCategoryId() {
        UUID uuid = UUID.randomUUID();
        Product product = new Product();
        product.setCategoryId(null);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        Product result = productService.getProductDetails(uuid.toString());
        assertNull(result.getCategory());
    }

    // getProductCategory tests
    @Test
    void getProductCategory_success() {
        when(categoryRepository.getCategoryHierarchy()).thenReturn(List.of());
        String result = productService.getProductCategory();
        assertNotNull(result);
    }

    @Test
    void getProductCategory_nullHierarchy() {
        when(categoryRepository.getCategoryHierarchy()).thenReturn(null);
        String result = productService.getProductCategory();
        assertNotNull(result);
    }

    // addProduct tests
    @Test
    void addProduct_success() {
        Product product = new Product();
        when(productRepository.save(any())).thenReturn(product);
        Product result = productService.addProduct(product);
        assertNotNull(result);
    }
}