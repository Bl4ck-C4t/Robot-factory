package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="clients")
public class Client {


    public Client(String name) {
        this.name = name;
    }

    public Client(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties("client")
    public List<Order> orders;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    public Location getLocation() {
        return location;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client(){

    }
}
