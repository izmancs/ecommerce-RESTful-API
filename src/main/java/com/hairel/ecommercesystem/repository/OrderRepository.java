package com.hairel.ecommercesystem.repository;

import com.hairel.ecommercesystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query(value = "SELECT ORDER_ID FROM ORDERS " +
            "WHERE CART_ID = :cartId " +
            "AND PRODUCT_ID = :productId", nativeQuery = true)
    Optional<Integer> findOrderIdByCartIdAndProductId(@Param("cartId") int cartId, @Param("productId") int productId);

    @Query(value = "SELECT ORDER_ID FROM ORDERS " +
            "WHERE CART_ID = :cartId ", nativeQuery = true)
    Optional<Integer> findOrderIdByCartId(@Param("cartId") int cartId);
}
