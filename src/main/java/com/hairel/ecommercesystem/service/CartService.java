package com.hairel.ecommercesystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    public String addToCart(int cartId, int productId, int quantity);

    public Page<Object[]> getListInCart(Pageable pageable);

    public List<Object[]> getListInCartById(int cartId);

    public void removeProductFromCart(int cartId, int productId);

}
