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
import com.example.project.model.Carrier;
import com.example.project.repository.CarrierRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CarrierController {

    private final Logger log = LoggerFactory.getLogger(CarrierController.class);

    private static final String ENTITY_NAME = "carrierController";  

    @Autowired
    CarrierRepository carrierRepository;

	public CarrierController(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @GetMapping("/carrier")
    public ResponseEntity<List<Carrier>> getAllCarriers(@ApiParam Pageable pageable) {
    	log.debug("REST request to get Carriers  {}");
    	Page<Carrier> page = carrierRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), null, HttpStatus.OK);
    }

    @GetMapping("/carrier/{id}")
    public ResponseEntity<Carrier> getConfigByCarrierId(@PathVariable Long id) {
        log.debug("REST request to get Carrier : {}", id);
        Carrier carrier = carrierRepository.findById(id).get();
        return new ResponseEntity<Carrier>(carrier, HttpStatus.OK);
    }

    @PostMapping("/carrier")
    public ResponseEntity<Carrier> createCarrier(@Valid @RequestBody Carrier carrier) throws Exception {
        log.debug("REST request to save Carrier : {}", carrier);
        if (carrier.getId() != null) {
            throw new Exception("A new carrier cannot already have an ID");
        }
        Carrier result = carrierRepository.save(carrier);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/carrier")
    public ResponseEntity<Carrier> updateCarrier(@Valid @RequestBody Carrier carrier) throws Exception {
        log.debug("REST request to update Carrier : {}", carrier);
        if (carrier.getId() == null) {
            return createCarrier(carrier);
        }
        Carrier result = carrierRepository.save(carrier);
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/carrier/{id}")
    public ResponseEntity<Void> deleteCarrier(@PathVariable Long id) {
        log.debug("REST request to delete Carrier : {}", id);
        carrierRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}




    
   
    
