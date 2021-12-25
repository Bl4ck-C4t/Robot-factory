package com.example.demo.reporsitories;

import com.example.demo.entities.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactoryRepo extends JpaRepository<Factory, Long> {
    public void deleteFactoryByName(String name);
    public Optional<Factory> findFactoryByName(String name);
}
