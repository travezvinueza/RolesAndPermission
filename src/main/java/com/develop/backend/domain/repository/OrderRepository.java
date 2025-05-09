package com.develop.backend.domain.repository;

import com.develop.backend.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :idUser")
    List<Order> findAllOrdersByUserId(@Param("idUser") Long idUser);
    @Query("SELECT o FROM Order o WHERE o.id = :idOrder AND o.user.id = :idUser")
    Optional<Order> findByIdAndUserId(@Param("idUser") Long idUser, @Param("idOrder") Long idOrder);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

}
