package com.develop.backend.domain.repository;

import com.develop.backend.application.dto.projection.OrderDetailProjection;
import com.develop.backend.domain.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query(value = "SELECT " +
            "od.id AS id, " +
            "p.product_code AS productCode, " +
            "o.description AS description, " +
            "p.product_name AS productName, " +
            "od.quantity AS quantity, " +
            "od.unit_price AS unitPrice, " +
            "(od.unit_price * od.quantity) AS totalPrice " +
            "FROM order_details od " +
            "INNER JOIN products p ON od.product_id = p.id " +
            "INNER JOIN orders o ON od.order_id = o.id " +
            "WHERE o.user_id = :idUser AND od.order_id IN :idOrder",
            nativeQuery = true)
    List<OrderDetailProjection> findAllByUserIdAndOrderId(Long idUser, List<Long> idOrder);

    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :idOrder")
    List<OrderDetail> findByOrderId(Long idOrder);

    @Query(value = "SELECT SUM(od.quantity * od.unit_price) AS totalPay " +
            "FROM order_details od " +
            "INNER JOIN orders o ON o.id = od.order_id " +
            "WHERE o.user_id = :id",
            nativeQuery = true)
    BigDecimal findTotalPriceByUserId(Long id);

    @Query("SELECT SUM(od.unitPrice * od.quantity) FROM OrderDetail od WHERE od.order.user.id = :userId AND od.order.id IN :orderIds")
    BigDecimal findTotalPriceByUserIdAndOrderIds(@Param("userId") Long userId, @Param("orderIds") List<Long> orderIds);

}
