package com.hairel.ecommercesystem.controller;

import com.hairel.ecommercesystem.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/addtocart")
    public String addToCart(@RequestParam(name = "cart_id", defaultValue = "-1") int cartId,
                            @RequestParam(name = "product_id") int productId,
                            @RequestParam(name = "quantity") int quantity) {

        return cartService.addToCart(cartId,productId,quantity);
    }

    @GetMapping("/cart")
    public ResponseEntity<Page<Object[]>> getListInCart(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "sortBy", defaultValue = "cart_id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Object[]> pageResult = cartService.getListInCart(pageable);

        return ResponseEntity.ok().body(pageResult);
    }

    @GetMapping("/cart/{id}")
    public List<Object[]> getListInCartById(@PathVariable("id") int cartId) {
        return cartService.getListInCartById(cartId);
    }

    @DeleteMapping("/cart")
    public String removeProductFromCart(@RequestParam(name = "cart_id", defaultValue = "-1") int cartId,
                                        @RequestParam(name = "product_id", defaultValue = "-1") int productId) {

        cartService.removeProductFromCart(cartId,productId);

        return "Product removed from Shopping Cart.";
    }

}
