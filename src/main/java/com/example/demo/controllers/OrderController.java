package com.example.demo.controllers;

import com.example.demo.entities.*;
import com.example.demo.reporsitories.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderRepo orderRepo;
    private final ClientRepo clientRepo;
    private final RobotRepo robotRepo;
    private final OrderedRobotRepo orderedRobotRepo;
    private final LocationRepo locationRepo;

    public OrderController(OrderRepo orderRepo, ClientRepo clientRepo, RobotRepo robotRepo, OrderedRobotRepo orderedRobotRepo, LocationRepo locationRep) {
        this.orderRepo = orderRepo;
        this.clientRepo = clientRepo;
        this.robotRepo = robotRepo;
        this.orderedRobotRepo = orderedRobotRepo;
        this.locationRepo = locationRep;
    }

    @GetMapping(value = "/fetch")
    @PreAuthorize("hasAnyAuthority('ROLE_ENGINEER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    @PostMapping(value = "/makeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeOrder(HttpServletResponse response,
                            @RequestParam Date deliveryTime,
                            @RequestParam String clientName,
                            @RequestParam double deliveryPrice,
                            @RequestParam List<Long> robotIds,
                            @RequestParam List<Integer> robotQuantities,
                            @RequestParam String city,
                            @RequestParam String address ){
        Optional<Client> clientOpt = clientRepo.findByName(clientName);
        if(clientOpt.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No such client!";
        }
        Optional<Location> locationOpt = locationRepo.getLocationByCityAndAddress(city, address);
        if(locationOpt.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No such location!";
        }
        int index = 0;
        Set<OrderedRobot> newOrderedRobots = new HashSet<>();
        for (Long robot_id: robotIds){
            Integer quantity = robotQuantities.get(index);

            Optional<Robot> robotOpt = robotRepo.findById(robot_id);
            if(robotOpt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return "No robot with that id.";
            }
            OrderedRobot orderedRobot = new OrderedRobot();
            orderedRobot.quantity = quantity;
            orderedRobot.robot = robotOpt.get();
            orderedRobotRepo.save(orderedRobot);
            newOrderedRobots.add(orderedRobot);
        }
        Order newOrder = new Order();
        newOrder.deliveryTime = deliveryTime;
        newOrder.address = locationOpt.get();
        newOrder.client = clientOpt.get();
        newOrder.delivered = false;
        newOrder.deliveryPrice = deliveryPrice;
        newOrder.robots = newOrderedRobots;

        orderRepo.save(newOrder);

        return "Order created.";
    }
}