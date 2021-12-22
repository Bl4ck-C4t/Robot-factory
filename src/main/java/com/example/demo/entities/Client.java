package com.example.demo.entities;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="clients")
public class Client {


    public Client(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "client")
    public List<Order> orders;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client(){

    }
}
