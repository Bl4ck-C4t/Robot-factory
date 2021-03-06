package com.example.demo.reporsitories;

import com.example.demo.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name);

    @Query("SELECT c FROM Client c " +
            "where (c.name = :firstName or :firstName is NULL) "
    )
    Page<Client> filterClients(Pageable pageable, String firstName);
}
