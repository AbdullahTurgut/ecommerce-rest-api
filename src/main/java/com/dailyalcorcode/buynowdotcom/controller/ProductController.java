package com.dailyalcorcode.buynowdotcom.controller;

import com.dailyalcorcode.buynowdotcom.dtos.ProductDto;
import com.dailyalcorcode.buynowdotcom.model.Product;
import com.dailyalcorcode.buynowdotcom.request.AddProductRequest;
import com.dailyalcorcode.buynowdotcom.request.ProductUpdateRequest;
import com.dailyalcorcode.buynowdotcom.response.ApiResponse;
import com.dailyalcorcode.buynowdotcom.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all-products")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Found!", productDto));
    }

    // Assignment 3 =====================================================================

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        Product product = productService.addProduct(request);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId,
                                                     @RequestBody ProductUpdateRequest request) {
        Product product = productService.updateProduct(productId, request);
        ProductDto productDto = productService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully", productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully!", productId));
    }

    @GetMapping("/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam("category") String category,
                                                                     @RequestParam("brand") String brand) {
        List<Product> productsByCategoryAndBrand = productService.getProductsByCategoryAndBrand(category, brand);
        List<ProductDto> productDtos = productService.getConvertedProducts(productsByCategoryAndBrand);
        return ResponseEntity.ok(new ApiResponse("Products found by category and brand", productDtos));
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam("brand") String brand,
                                                                 @RequestParam("name") String name) {
        List<Product> productsByBrandAndName = productService.getProductsByBrandAndName(brand, name);
        List<ProductDto> productDtos = productService.getConvertedProducts(productsByBrandAndName);
        return ResponseEntity.ok(new ApiResponse("Products found by category and brand", productDtos));
    }

    @GetMapping("/by-category/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products found by category", productDtos));
    }

    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<ApiResponse> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products found by category", productDtos));
    }

    @GetMapping("/by-brand/{brand}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String brand) {
        List<Product> products = productService.getProductsByBrand(brand);
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products found by brand", productDtos));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        List<Product> products = productService.getProductsByName(name);
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products found by name", productDtos));
    }

    @GetMapping("/distinct/products")
    public ResponseEntity<ApiResponse> getDistinctProductsByName() {
        List<Product> products = productService.findDistinctProductsByName();
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found", productDtos));
    }

    @GetMapping("/distinct/brands")
    public ResponseEntity<ApiResponse> getAllDistinctBrands() {
        return ResponseEntity.ok(new ApiResponse("Found", productService.getAllDistinctBrands()));
    }
}
