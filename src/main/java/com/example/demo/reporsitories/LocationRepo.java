package com.example.demo.reporsitories;

import com.example.demo.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Long> {

}
