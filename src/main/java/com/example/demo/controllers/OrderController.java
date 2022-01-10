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
@PreAuthorize("hasAnyAuthority('ROLE_SALESMAN', 'ROLE_ADMIN')")
public class OrderController {

    private final OrderRepo orderRepo;


    public OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping(value = "/fetch")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    @PutMapping(value = "/{order_id}/changePrice")
    @ResponseStatus(HttpStatus.OK)
    public String getAllOrders(@PathVariable Long order_id, @RequestParam Double price){
        Optional<Order> order = orderRepo.findById(order_id);
        if(order.isPresent()){
            order.get().deliveryPrice = price;
            orderRepo.save(order.get());
            return "Price changed";
        }
        return "No such order.";

    }




}
