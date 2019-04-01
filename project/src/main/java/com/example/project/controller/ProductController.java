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
import com.example.project.model.Product;
import com.example.project.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    private static final String ENTITY_NAME = "productController";  

    @Autowired
    ProductRepository productRepository;

	public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts(@ApiParam Pageable pageable) {
    	log.debug("REST request to get Products  {}");
    	Page<Product> page = productRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), null, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getConfigByProductId(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Product product = productRepository.findById(id).get();
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws Exception {
        log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            throw new Exception("A new product cannot already have an ID");
        }
        Product result = productRepository.save(product);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/product")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) throws Exception {
        log.debug("REST request to update Product : {}", product);
        if (product.getId() == null) {
            return createProduct(product);
        }
        Product result = productRepository.save(product);
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}




    
   
    
