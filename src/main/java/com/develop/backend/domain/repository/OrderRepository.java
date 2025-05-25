package com.develop.backend.domain.repository;

import com.develop.backend.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :idUser")
    List<Order> findAllOrdersByUserId(@Param("idUser") Long idUser);
    @Query("SELECT o FROM Order o WHERE o.id IN :idOrder AND o.user.id = :idUser")
    List<Order> findByIdAndUserId(@Param("idUser") Long idUser, @Param("idOrder") List<Long> idOrder);
}
