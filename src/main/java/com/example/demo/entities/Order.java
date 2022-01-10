package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "robot_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public Date deliveryTime;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    public Location address;

    public boolean delivered;

    public double deliveryPrice;

    @ManyToOne
    @JoinColumn(name = "client_id")

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @ManyToMany
    @JoinTable(
            name = "order_robots",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "ordered_robot_id")
    )
    public Set<OrderedRobot> robots;

    public void updatePrice(){
        deliveryPrice = 0;
        robots.forEach(robo -> deliveryPrice += robo.robot.cost * robo.quantity);
        deliveryPrice += robots.size() * 2;
    }
}
