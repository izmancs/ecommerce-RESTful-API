package com.hairel.ecommercesystem.service;

import com.hairel.ecommercesystem.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {
    public void addToCart(int productId, int quantity);

    public Page<Cart> getListInCart(Pageable pageable);

    public void removeProductFromCart(int productId);

}
