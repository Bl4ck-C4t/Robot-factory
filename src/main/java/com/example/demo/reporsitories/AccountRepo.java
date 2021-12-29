package com.example.demo.reporsitories;

import com.example.demo.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
    Account findAccountByUsername(String username);
}
