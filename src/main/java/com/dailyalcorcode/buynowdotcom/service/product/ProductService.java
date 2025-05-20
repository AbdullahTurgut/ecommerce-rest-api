package com.dailyalcorcode.buynowdotcom.service.product;

import com.dailyalcorcode.buynowdotcom.model.*;
import com.dailyalcorcode.buynowdotcom.repository.CartItemRepository;
import com.dailyalcorcode.buynowdotcom.repository.CategoryRepository;
import com.dailyalcorcode.buynowdotcom.repository.OrderItemRepository;
import com.dailyalcorcode.buynowdotcom.repository.ProductRepository;
import com.dailyalcorcode.buynowdotcom.request.AddProductRequest;
import com.dailyalcorcode.buynowdotcom.request.ProductUpdateRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        if (productExists(request.getName(), request.getBrand())) {
            throw new EntityExistsException(request.getName() + " already exists!");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(Long productId, ProductUpdateRequest request) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new EntityNotFoundException("Product Not Found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product Not Found!"));
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(product -> {
            // Remove CartItem from Product Entity
            List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
            cartItems.forEach(cartItem -> {
                Cart cart = cartItem.getCart();
                cart.removeItem(cartItem);
                cartItemRepository.delete(cartItem);
            });

            // Remove OrderItem from Product Entity
            List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
            orderItems.forEach(orderItem -> {
                orderItem.setProduct(null);
                orderItemRepository.save(orderItem);
            });

            // Remove Category from Product Entity
            Optional.ofNullable(product.getCategory())
                    .ifPresent(category -> category.getProducts().remove(product));
            product.setCategory(null);

            productRepository.deleteById(productId);
        }, () -> {
            throw new EntityNotFoundException("Product Not Found!");
        });
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
}
