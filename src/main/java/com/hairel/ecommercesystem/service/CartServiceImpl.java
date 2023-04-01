package com.hairel.ecommercesystem.service;

import com.hairel.ecommercesystem.entity.Cart;
import com.hairel.ecommercesystem.entity.Order;
import com.hairel.ecommercesystem.entity.Product;
import com.hairel.ecommercesystem.repository.CartRepository;
import com.hairel.ecommercesystem.repository.OrderRepository;
import com.hairel.ecommercesystem.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Override
    public String addToCart(int cartId, int productId, int quantity) {

        //Cart cart = cartRepository.findById(cartId).get();
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Optional<Cart> optionalCart = cartRepository.findById(cartId);

        //if cart is existing, add new product to cart.
        if(optionalCart.isPresent()) {

            Cart cart = optionalCart.get();

            Optional<Integer> optionalOrderId = orderRepository.findOrderIdByCartIdAndProductId(cartId, productId);

            //no need to add new product, just add quantity
            if(optionalOrderId.isPresent()) {

                int orderId = optionalOrderId.get();

                Order orderUpdated = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));

                orderUpdated.setQuantity(orderUpdated.getQuantity()+quantity);

                orderRepository.save(orderUpdated);

                BigDecimal originalTotalAmount = cartRepository.getTotalAmountByCartId(cartId);
                BigDecimal productUnitPrice = product.getUnitPrice();
                BigDecimal totalAddedPrice = productUnitPrice.multiply(BigDecimal.valueOf(quantity));
                BigDecimal totalAmountUpdated = originalTotalAmount.add(totalAddedPrice);

                cart.setTotalAmount(totalAmountUpdated);

                cartRepository.save(cart);

                return "Added existing product to cart, quantity added";
            }
            //add new product to existing cart.
            else {
                Order newOrder = new Order();
                newOrder.setProduct(product);
                newOrder.setCart(cart);
                newOrder.setQuantity(quantity);

                orderRepository.save(newOrder);

                BigDecimal originalTotalAmount = cartRepository.getTotalAmountByCartId(cartId);
                BigDecimal productUnitPrice = product.getUnitPrice();
                BigDecimal totalAddedPrice = productUnitPrice.multiply(BigDecimal.valueOf(quantity));
                BigDecimal totalAmountUpdated = originalTotalAmount.add(totalAddedPrice);

                cart.setTotalAmount(totalAmountUpdated);

                cartRepository.save(cart);

                return "Added new product to cart";
            }
        }
        // Create new cart.
        else {

            Cart newCart = new Cart();

            BigDecimal productUnitPrice = product.getUnitPrice();
            BigDecimal totalAddedPrice = productUnitPrice.multiply(BigDecimal.valueOf(quantity));

            newCart.setTotalAmount(totalAddedPrice);

            cartRepository.save(newCart);

            Order newOrder = new Order();
            newOrder.setProduct(product);
            newOrder.setCart(newCart);
            newOrder.setQuantity(quantity);

            orderRepository.save(newOrder);

            return "New cart created, product added to the cart";

        }
    }

    @Override
    public Page<Object[]> getListInCart(Pageable pageable) {
        //return cartRepository.findAll(pageable);

        return cartRepository.getListProductsInCart(pageable);

    }

    @Override
    public List<Object[]> getListInCartById(int cartId) {

        return cartRepository.getListProductsInCartByCartId(cartId);
    }

    @Override
    public void removeProductFromCart(int cartId, int productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        int orderId;
        Order order = new Order();

        Optional<Integer> optionalOrderId = orderRepository.findOrderIdByCartIdAndProductId(cartId, productId);
        if (optionalOrderId.isPresent()) {
            orderId = optionalOrderId.get();
            order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        } else {
            throw new EntityNotFoundException("Order not found");
        }

        int quantity = cartRepository.getQuantityByProductIdAndCartId(productId,cartId);

        if(quantity>1) {
            order.setQuantity(quantity-1);
            orderRepository.save(order);

            //cart
            BigDecimal originalTotalAmount = cartRepository.getTotalAmountByCartId(cartId);
            BigDecimal productUnitPrice = product.getUnitPrice();
            BigDecimal totalAmountUpdated = originalTotalAmount.subtract(productUnitPrice);

            cart.setTotalAmount(totalAmountUpdated);

            cartRepository.save(cart);

        } else {

            orderRepository.deleteById(orderId);

            Optional<Integer> optionalOrderId2 = orderRepository.findOrderIdByCartId(cartId);

            if(optionalOrderId2.isPresent()){

                BigDecimal originalTotalAmount = cartRepository.getTotalAmountByCartId(cartId);
                BigDecimal productUnitPrice = product.getUnitPrice();
                BigDecimal totalAmountUpdated = originalTotalAmount.subtract(productUnitPrice);

                cart.setTotalAmount(totalAmountUpdated);

                cartRepository.save(cart);

            } else {
                cartRepository.deleteById(cartId);
            }

        }

    }


}
