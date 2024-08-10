package com.example.user.service.services.impl;

import com.example.user.service.entities.Hotel;
import com.example.user.service.entities.Rating;
import com.example.user.service.entities.User;
import com.example.user.service.exceptions.ResourceNotFoundException;
import com.example.user.service.external.services.HotelService;
import com.example.user.service.repositories.UserRepository;
import com.example.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //Generate unique userId
        String randomUserId =  UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //TODO: Implement get rating
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with this id not found ->" + userId));
        //fetch rating from RATINGSERVICE
        Rating[] ratingsOfUser =   restTemplate.getForObject("http://RATINGSERVICE/ratings/users/" + user.getUserId() , Rating[].class);
        logger.info("{}" , ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

        List<Rating> ratingList =  ratings.stream().map(rating -> {

            //API to go hotel service to get hotel
            //Call using REST TEMPLATE
            //ResponseEntity<Hotel> forEntity =  restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            // Hotel hotel = forEntity.getBody();
            //logger.info("Response code for getting hotel : {}", forEntity.getStatusCode());

            //Call using FEIGN
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            //set hotel to rating
            rating.setHotel(hotel);

            //return the rating
            return rating;

        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }


}
