package com.hairel.ecommercesystem.controller;

import com.hairel.ecommercesystem.entity.Product;
import com.hairel.ecommercesystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product/add")
    public String addProduct(@RequestBody Product product) {

        productService.addProduct(product);
        return "New Product Added";
    }

    @GetMapping("/product")
    public Page<Product> getAllProduct(@RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "10") int size,
                                       @RequestParam(name = "sortBy", defaultValue = "productId") String sortBy) {

        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.getAllProduct(pageable);
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable("id") int productId) {

        return productService.getProductById(productId);
    }
}
