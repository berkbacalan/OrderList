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
import com.example.project.model.User;
import com.example.project.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String ENTITY_NAME = "UserController";  

    @Autowired
    UserRepository userRepository;

	public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(@ApiParam Pageable pageable) {
    	log.debug("REST request to get Users  {}");
    	Page<User> page = userRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), null, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getConfigByUserId(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        User user = userRepository.findById(id).get();
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws Exception {
        log.debug("REST request to save User : {}", user);
        if (user.getId() != null) {
            throw new Exception("A new user cannot already have an ID");
        }
        User result = userRepository.save(user);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws Exception {
        log.debug("REST request to update User : {}", user);
        if (user.getId() == null) {
            return createUser(user);
        }
        User result = userRepository.save(user);
        return ResponseEntity.ok().body(result);
    }
    
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}




    
   
    
