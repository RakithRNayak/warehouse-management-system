package com.wms.inventory.service;

import com.wms.inventory.entity.Category;
import com.wms.inventory.entity.Product;
import com.wms.inventory.exception.DuplicateResourceException;
import com.wms.inventory.exception.ResourceNotFoundException;
import com.wms.inventory.repository.CategoryRepository;
import com.wms.inventory.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(String search, Pageable pageable) {
        return productRepository.searchProducts(search, pageable);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }

    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + product.getSku() + " already exists");
        }
        if (product.getBarcode() != null && productRepository.existsByBarcode(product.getBarcode())) {
            throw new DuplicateResourceException("Product with barcode " + product.getBarcode() + " already exists");
        }
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setUnitOfMeasure(productDetails.getUnitOfMeasure());
        product.setWeight(productDetails.getWeight());
        product.setLength(productDetails.getLength());
        product.setWidth(productDetails.getWidth());
        product.setHeight(productDetails.getHeight());
        product.setBarcode(productDetails.getBarcode());
        product.setImageUrl(productDetails.getImageUrl());
        product.setMinStockLevel(productDetails.getMinStockLevel());
        product.setMaxStockLevel(productDetails.getMaxStockLevel());
        product.setReorderPoint(productDetails.getReorderPoint());
        product.setUnitCost(productDetails.getUnitCost());
        if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findById(productDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setIsActive(false);
        productRepository.save(product);
    }
}
