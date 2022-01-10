package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String username;
    public String password;
    public String role;

    @OneToOne(mappedBy = "account")
    @JsonIgnoreProperties("account")
    private Client client;

    public Client getClient() {
        return client;
    }

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Account() {
    }
}
