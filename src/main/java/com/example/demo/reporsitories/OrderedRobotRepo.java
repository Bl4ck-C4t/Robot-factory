package com.example.demo.reporsitories;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderedRobot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedRobotRepo extends JpaRepository<OrderedRobot, Long> {

}
