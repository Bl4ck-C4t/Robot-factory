package com.example.demo.controllers;

import com.example.demo.entities.*;

import com.example.demo.reporsitories.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/client")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENT')")
public class ClientController {

    private final ClientRepo clientRepo;
    private final AccountRepo accountRepo;
    private final LocationRepo locationRepo;
    private final CityRepo cityRepo;
    private final RobotRepo robotRepo;
    private final OrderRepo orderRepo;
    private final OrderedRobotRepo orderedRobotRepo;


    public ClientController(ClientRepo repo, AccountRepo accountRepo, LocationRepo locationRepo, CityRepo cityRepo, RobotRepo robotRepo, OrderRepo orderRepo, OrderedRobotRepo orderedRobotRepo){
        this.clientRepo = repo;
        this.accountRepo = accountRepo;
        this.locationRepo = locationRepo;
        this.cityRepo = cityRepo;
        this.robotRepo = robotRepo;
        this.orderRepo = orderRepo;
        this.orderedRobotRepo = orderedRobotRepo;
    }

    @GetMapping(value = "/fetch")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Client> getClients(){
        return clientRepo.findAll();
    }

    @GetMapping(value = "/order/fetch")
    public List<Order> getClientOrders(HttpServletResponse resp, Principal princ, String clientName){

        Client client = ClientFromPrinc(princ, clientName);
        if(client == null)
            return null;

        return client.orders;

    }

    @GetMapping(value = "/getClient")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getClient(String fname, String lastName){
        Optional<Client> client = clientRepo.findByName(fname);
        if(client.isPresent()){
            return ResponseEntity.ok(client);
        }
        return ResponseEntity.notFound().build();
    }

    public Location createLocation(@RequestParam String address, @RequestParam String cityName){
        Optional<City> cityOpt = cityRepo.findCityByCityName(cityName);
        City city = cityOpt.orElseGet(() -> cityRepo.save(new City(cityName)));
        Location loc = new Location(city, address);
        locationRepo.save(loc);
        return loc;
    }

    @PostMapping(value = "/makeClient")
    @ResponseStatus(HttpStatus.CREATED)
    public String createClient(Principal princ, @RequestParam String fname,
                               @RequestParam String address,
                               @RequestParam String city){
        Optional<Location> locOpt = locationRepo.getLocationByCityAndAddress(city, address);
        Location loc = locOpt.orElse(createLocation(address, city));
        Account acc = accountRepo.findAccountByUsername(princ.getName());
        Client client = new Client(fname, loc);
        client.setAccount(acc);
        clientRepo.save(client);
        return "Client Created!";
    }

    @DeleteMapping(value = "/deleteClient")
    public String deleteClient(HttpServletResponse response, String fname){
        Optional<Client> ToDelete = clientRepo.findByName(fname);
        if(ToDelete.isEmpty()){
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "Client not found :(";
        }
        clientRepo.delete(ToDelete.get());
        return "Client Deleted!";
    }

    @PostMapping(value = "/orders/makeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String makeOrder(HttpServletResponse response,
                            Principal princ,
                            String clientName,
                            @RequestParam
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date deliveryTime,
                            @RequestParam List<Long> robotIds,
                            @RequestParam List<Integer> robotQuantities,
                            String city,
                            String address ){
        Client client = ClientFromPrinc(princ, clientName);

        if (client == null) return "Cannot find client";

        Location loc;

        if(city != null && address != null){
            Optional<Location> locationOpt = locationRepo.getLocationByCityAndAddress(city, address);
            if(locationOpt.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return "No such location!";
            }
            loc = locationOpt.get();
        }
        else
            loc = client.getLocation();


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
            index++;
        }
        Order newOrder = new Order();
        newOrder.deliveryTime = deliveryTime;
        newOrder.address = loc;
        newOrder.setClient(client);
        newOrder.delivered = false;
        newOrder.robots = newOrderedRobots;
        newOrder.updatePrice();

        orderRepo.save(newOrder);

        return "Order created.";
    }

    @PutMapping(value = "/orders/{orderId}/addRobot")
    @ResponseStatus(HttpStatus.CREATED)
    public String addRobot(HttpServletResponse response,
                           Principal princ,
                           String clientName,
                           @PathVariable Long orderId, @RequestParam String robotName){

        Client client = ClientFromPrinc(princ, clientName);
        if (client == null) return "Cannot find client";
        Order order = orderRepo.findById(orderId).orElse(null);
        if(order == null || !client.orders.contains(order)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No order with that id.";
        }

        Optional<Robot> robotOpt = robotRepo.findRobotByName(robotName);
        if(robotOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No robot with that name.";
        }

        Robot robot = robotOpt.get();
        Optional<OrderedRobot> orderRobotOpt = order.robots.stream()
                .filter((orderedRobot)-> Objects.equals(orderedRobot.robot.getId(), robot.getId()))
                .findFirst();
        if(orderRobotOpt.isPresent()){
            orderRobotOpt.get().quantity++;
        }
        else{
            OrderedRobot orderedRobot = new OrderedRobot();
            orderedRobot.robot = robot;
            orderedRobot.quantity = 1;
            order.robots.add(orderedRobot);
        }
        order.updatePrice();
        orderRepo.save(order);

        return "Robot added";
    }

    @DeleteMapping(value = "/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOrder(HttpServletResponse response,
                           Principal princ,
                           String clientName,
                           @PathVariable Long orderId) {

        Client client = ClientFromPrinc(princ, clientName);
        if (client == null) return "Cannot find client";
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order == null || !client.orders.contains(order)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return "No order with that id.";
        }

        orderRepo.deleteById(orderId);
        return "Deleted order";

    }

    private Client ClientFromPrinc(Principal princ, String clientName) {
        Account acc = accountRepo.findAccountByUsername(princ.getName());
        Client client;
        if(acc.role.equals("ROLE_ADMIN")){
            client = clientRepo.findByName(clientName).orElse(null);
        }
        else{
            client = acc.getClient();
        }
        return client;
    }

/*    @GetMapping("/filter")
    public ResponseEntity<?> filterClients( String fname, int currentPage, int perPage){
        Pageable pagable = PageRequest.of(currentPage - 1, perPage);
        Page<Client> clientPage = clientRepo.filterClients(pagable, fname);
        Map<String, Object> response = new HashMap<>();
        response.put("pages", clientPage.getTotalPages());
        response.put("elements", clientPage.getTotalElements());
        response.put("clients", clientPage.getContent());

        return ResponseEntity.ok(response);
    }*/

}
