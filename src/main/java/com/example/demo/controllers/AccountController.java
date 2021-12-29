package com.example.demo.controllers;


import com.example.demo.entities.Account;
import com.example.demo.reporsitories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/")
public class AccountController {
    private final AccountRepo accountRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AccountController(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@RequestParam String username, @RequestParam String password){
        Account acc = new Account(username, passwordEncoder.encode(password), "ROLE_CLIENT");
        accountRepo.save(acc);
        return "Account saved!";
    }

    @PutMapping(value = "/assignRole")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String giveRole(@RequestParam String username, @RequestParam String role){
        Account acc = accountRepo.findAccountByUsername(username);
        if (acc == null)
            return "No such account!";
        String old_role = acc.role;
        acc.role = "ROLE_" + role;
        accountRepo.save(acc);

        return String.format("Changed role from %s to 'ROLE_%s'!", old_role, role);

    }

    @GetMapping(value = "/fetchUsers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Account> getUsers(){
        return accountRepo.findAll();
    }

    @GetMapping(value = "/logged")
    public String giveLoggedInfo(Authentication auth){
        UserDetails user = (UserDetails) auth.getPrincipal();
        return "Logged in as " + user.getUsername() + "\n with role: " + user
                .getAuthorities().stream().findFirst().get();
    }


}
