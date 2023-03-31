package com.hairel.ecommercesystem.service;

import com.hairel.ecommercesystem.entity.Cart;
import com.hairel.ecommercesystem.entity.Product;
import com.hairel.ecommercesystem.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductService productService;

    @Override
    public void addToCart(int productId, int quantity) {

        boolean found = false;

        Cart cartUpdated = new Cart();

        Product product = productService.getProductById(productId);
        List<Cart> cartList = cartRepository.findAll();

        for(Cart c:cartList){
            if(c.getProduct().equals(product)){
                found = true;
                cartUpdated = c;
                break;
            }
        }

        if(found) {
            cartUpdated.setQuantity(cartUpdated.getQuantity()+quantity);
            cartRepository.save(cartUpdated);
        } else {
            cartUpdated.setProduct(product);
            cartUpdated.setUnitPrice(product.getUnitPrice());
            cartUpdated.setQuantity(quantity);
            cartUpdated.setTotalAmount(product.getUnitPrice()*quantity);
            cartRepository.save(cartUpdated);
        }

    }

    @Override
    public Page<Cart> getListInCart(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    public void removeProductFromCart(int productId) {

        Cart cartUpdated = cartRepository.findById(productId).get();

        // - 1 quantity for the selected product
        if (cartUpdated.getQuantity()>1){
            cartUpdated.setQuantity(cartUpdated.getQuantity()-1);
            cartRepository.save(cartUpdated);
        }
        // remove product when quantity=0
        else
            cartRepository.delete(cartUpdated);

    }

}
