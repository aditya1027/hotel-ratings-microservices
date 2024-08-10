package com.example.user.service.controllers;


import com.example.user.service.entities.User;
import com.example.user.service.services.UserService;
import com.example.user.service.services.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){

        User user1 =  userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //single user
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker" , fallbackMethod = "ratingHotelFallback")
    //@Retry(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
   @RateLimiter(name = "userRateLimiter" , fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User user1 = userService.getUser(userId);
        return ResponseEntity.ok(user1);
    }

    public ResponseEntity<User> ratingHotelFallback(String userId , Exception ex){
        User user = User.builder().
                email("dummy mail")
                .name("Dummy")
                .about("dummy about")
                .build();

        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    //all users get
    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }


}
