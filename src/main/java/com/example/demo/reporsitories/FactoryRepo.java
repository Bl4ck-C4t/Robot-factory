package com.example.demo.reporsitories;

import com.example.demo.entities.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepo extends JpaRepository<Factory, Long> {
}
