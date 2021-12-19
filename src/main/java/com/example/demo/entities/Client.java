package com.example.demo.entities;


import javax.persistence.*;

@Entity
@Table(name="clients")
public class Client {


    public Client(String name, String lastName, int num) {
        this.name = name;
        this.lastName = lastName;
        this.num = num;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String name;

    @Column()
    private String lastName;

    @Column()
    private Integer num;

    @ManyToOne()
    @JoinColumn(name = "city_id")
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Client(){

    }
}
