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
import com.example.project.model.Market;
import com.example.project.repository.MarketRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MarketController {

    private final Logger log = LoggerFactory.getLogger(MarketController.class);

    private static final String ENTITY_NAME = "marketController";  

    @Autowired
    MarketRepository marketRepository;

	public MarketController(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    @GetMapping("/market")
    public ResponseEntity<List<Market>> getAllOrders(@ApiParam Pageable pageable) {
    	log.debug("REST request to get Markets  {}");
    	Page<Market> page = marketRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), null, HttpStatus.OK);
    }

    @GetMapping("/market/{id}")
    public ResponseEntity<Market> getConfigByOrderId(@PathVariable Long id) {
        log.debug("REST request to get Market : {}", id);
        Market market = marketRepository.findById(id).get();
        return new ResponseEntity<Market>(market, HttpStatus.OK);
    }

    @PostMapping("/market")
    public ResponseEntity<Market> createOrder(@Valid @RequestBody Market market) throws Exception {
        log.debug("REST request to save Market : {}", market);
        if (market.getId() != null) {
            throw new Exception("A new market cannot already have an ID");
        }
        Market result = marketRepository.save(market);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/market")
    public ResponseEntity<Market> updateOrder(@Valid @RequestBody Market market) throws Exception {
        log.debug("REST request to update Market : {}", market);
        if (market.getId() == null) {
            return createOrder(market);
        }
        Market result = marketRepository.save(market);
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/market/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Market : {}", id);
        marketRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}




    
   
    
