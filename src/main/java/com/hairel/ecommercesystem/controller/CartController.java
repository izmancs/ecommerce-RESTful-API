package com.hairel.ecommercesystem.controller;

import com.hairel.ecommercesystem.entity.Cart;
import com.hairel.ecommercesystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/addtocart")
    public String addToCart(@RequestParam(name = "product_id") int productId, @RequestParam(name = "quantity") int quantity) {

        cartService.addToCart(productId,quantity);

        return "Product added to Shopping Cart.";
    }

    @GetMapping("/cart")
    public Page<Cart> getListInCart(@RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                    @RequestParam(name = "sortBy", defaultValue = "cartId") String sortBy) {

        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);

        return cartService.getListInCart(pageable);
    }

    @DeleteMapping("/cart/{id}")
    public String removeProductFromCart(@PathVariable("id") int productId) {

        cartService.removeProductFromCart(productId);

        return "Product removed from Shopping Cart.";
    }

}
