package com.example.demo;

import com.example.demo.entities.Account;
import com.example.demo.reporsitories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService implements UserDetailsService {
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepo.findAccountByUsername(username);
        if(acc == null)
            throw new UsernameNotFoundException("No such username");
        return new User(acc.username, acc.password, List.of(new SimpleGrantedAuthority(acc.role)));
    }
}
