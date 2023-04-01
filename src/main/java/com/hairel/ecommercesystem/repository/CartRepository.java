package com.hairel.ecommercesystem.repository;

import com.hairel.ecommercesystem.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query(value = "SELECT c.total_amount " +
            "FROM CARTS c " +
            "JOIN ORDERS o ON c.cart_id = o.cart_id " +
            "JOIN PRODUCTS p ON p.product_id = o.product_id " +
            "WHERE c.cart_id = :cartId", nativeQuery = true)
    BigDecimal getTotalAmountByCartId(@Param("cartId") int cartId);

    @Query(value = "SELECT quantity " +
            "FROM CARTS c " +
            "JOIN ORDERS o ON c.cart_id = o.cart_id " +
            "JOIN PRODUCTS p ON p.product_id = o.product_id " +
            "WHERE p.product_id = :productId " +
            "AND c.cart_id = :cartId", nativeQuery = true)
    int getQuantityByProductIdAndCartId(@Param("productId") int productId, @Param("cartId") int cartId);

    @Query(value = "SELECT c.cart_id, p.product_name, o.quantity, p.unit_price, c.total_amount " +
            "FROM CARTS c " +
            "JOIN ORDERS o ON c.cart_id = o.cart_id " +
            "JOIN PRODUCTS p ON p.product_id = o.product_id " +
            "GROUP BY c.cart_id, p.product_id" ,
            countQuery = "SELECT COUNT(*) FROM (SELECT c.cart_id, p.product_name, o.quantity, p.unit_price, c.total_amount " +
                    "FROM CARTS c " +
                    "JOIN ORDERS o ON c.cart_id = o.cart_id " +
                    "JOIN PRODUCTS p ON p.product_id = o.product_id " +
                    "GROUP BY c.cart_id, p.product_id) AS count_table",
            nativeQuery = true)
    Page<Object[]> getListProductsInCart(Pageable pageable);

    @Query(value = "SELECT p.product_name, o.quantity, p.unit_price, c.total_amount " +
            "FROM CARTS c " +
            "JOIN ORDERS o ON c.cart_id = o.cart_id " +
            "JOIN PRODUCTS p ON p.product_id = o.product_id " +
            "WHERE c.cart_id = :cartId ", nativeQuery = true)
    List<Object[]> getListProductsInCartByCartId(@Param("cartId") int cartId);

}

