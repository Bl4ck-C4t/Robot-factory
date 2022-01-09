package com.example.demo.reporsitories;

import com.example.demo.entities.Order;
import com.example.demo.entities.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById (Long id);
}
