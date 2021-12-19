package com.example.demo.controllers;

import com.example.demo.entities.Client;

import com.example.demo.reporsitories.ClientRepo;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping(value = "/client")
public class ClientController {

    private final ClientRepo clientRepo;

    public ClientController(ClientRepo repo){
        this.clientRepo = repo;
    }

    @GetMapping(value = "/fetch")
    public List<Client> getClients(){
        return clientRepo.findAll();
    }

    @GetMapping(value = "/getClient")
    public ResponseEntity<?> getClient(String fname, String lastName){
        Optional<Client> client = clientRepo.findByNameAndLastName(fname, lastName);
        if(client.isPresent()){
            return ResponseEntity.ok(client);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/makeClient")
    @ResponseStatus(HttpStatus.CREATED)
    public String createClient(String fname, String lastName, Integer number){
        clientRepo.save(new Client(fname, lastName, number));
        return "Client Created!";
    }

    @DeleteMapping(value = "/deleteClient")
    public String deleteClient(HttpServletResponse response, String fname, String lastName){
        Optional<Client> ToDelete = clientRepo.findByNameAndLastName(fname, lastName);
        if(ToDelete.isEmpty()){
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "Client not found :(";
        }
        clientRepo.delete(ToDelete.get());
        return "Client Deleted!";
    }

    @GetMapping(value = "/makeTest")
    public int createEntity(){
        Client cl = new Client("mark", "ben", 5);
        clientRepo.save(cl);
        return 5;
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterClients( String fname, String lname, int currentPage, int perPage){
        Pageable pagable = PageRequest.of(currentPage - 1, perPage);
        Page<Client> clientPage = clientRepo.filterClients(pagable, fname, lname);
        Map<String, Object> response = new HashMap<>();
        response.put("pages", clientPage.getTotalPages());
        response.put("elements", clientPage.getTotalElements());
        response.put("clients", clientPage.getContent());

        return ResponseEntity.ok(response);
    }

}
