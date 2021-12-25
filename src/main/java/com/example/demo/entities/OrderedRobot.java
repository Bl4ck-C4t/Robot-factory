package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class OrderedRobot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer quantity;

    @ManyToOne()
    @JoinColumn(name = "robot_id")
    public Robot robot;
}
