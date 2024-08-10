package com.example.user.service.services;

import com.example.user.service.entities.User;

import java.util.List;

public interface UserService {

    //user operations

    //create
    User saveUser(User user);


    //Get all users
    List<User> getAllUser();


    //Get single user
    User getUser(String userId);

    //TODO: Delete

}

