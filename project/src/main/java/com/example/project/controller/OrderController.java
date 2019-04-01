package com.example.project.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import com.example.project.model.Order;
import com.example.project.repository.OrderRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private static final String ENTITY_NAME = "orderController";  

    @Autowired
    OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getAllOrders(@ApiParam Pageable pageable) {
    	log.debug("REST request to get Orders  {}");
    	Page<Order> page = orderRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), null, HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getConfigByOrderId(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Order order = orderRepository.findById(id).get();
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws Exception {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new Exception("A new order cannot already have an ID");
        }
        Order result = orderRepository.save(order);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/order")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) throws Exception {
        log.debug("REST request to update Order : {}", order);
        if (order.getId() == null) {
            return createOrder(order);
        }
        Order result = orderRepository.save(order);
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}




    
   
    
