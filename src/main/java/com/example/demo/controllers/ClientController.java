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
        Optional<Client> client = clientRepo.findByName(fname);
        if(client.isPresent()){
            return ResponseEntity.ok(client);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/makeClient")
    @ResponseStatus(HttpStatus.CREATED)
    public String createClient(String fname){
        clientRepo.save(new Client(fname));
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

    @GetMapping(value = "/makeTest")
    public int createEntity(){
        Client cl = new Client("mark");
        clientRepo.save(cl);
        return 5;
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterClients( String fname, int currentPage, int perPage){
        Pageable pagable = PageRequest.of(currentPage - 1, perPage);
        Page<Client> clientPage = clientRepo.filterClients(pagable, fname);
        Map<String, Object> response = new HashMap<>();
        response.put("pages", clientPage.getTotalPages());
        response.put("elements", clientPage.getTotalElements());
        response.put("clients", clientPage.getContent());

        return ResponseEntity.ok(response);
    }

}
