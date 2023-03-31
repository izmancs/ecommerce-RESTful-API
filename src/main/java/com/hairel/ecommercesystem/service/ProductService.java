package com.hairel.ecommercesystem.service;

import com.hairel.ecommercesystem.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    public void addProduct(Product product);

    public Page<Product> getAllProduct(Pageable pageable);

    public Product getProductById(int productId);

}
