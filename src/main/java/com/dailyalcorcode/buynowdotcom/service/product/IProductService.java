package com.dailyalcorcode.buynowdotcom.service.product;

import com.dailyalcorcode.buynowdotcom.dtos.ProductDto;
import com.dailyalcorcode.buynowdotcom.model.Product;
import com.dailyalcorcode.buynowdotcom.request.AddProductRequest;
import com.dailyalcorcode.buynowdotcom.request.ProductUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);

    Product updateProduct(Long productId, ProductUpdateRequest product);

    Product getProductById(Long productId);

    void deleteProductById(Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByBrandAndName(String brand, String name);

    List<Product> getProductsByCategory(String category);


    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByName(String name);

    List<Product> findDistinctProductsByName();

    List<String> getAllDistinctBrands();

    List<Product> searchProductsByImage(MultipartFile image) throws IOException;

    // Helper mapper List<product> to List<productDto>
    List<ProductDto> getConvertedProducts(List<Product> products);

    // Helper mapper product to productDto
    ProductDto convertToDto(Product product);

    List<Product> getProductsByCategoryId(Long categoryId);

}
