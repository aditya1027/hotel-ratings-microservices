package com.example.rating.services;

import com.example.rating.entities.Rating;

import java.util.List;

public interface RatingService
{
    //Create
    Rating createRating(Rating rating);

    //Get all
    List<Rating> getRatings();

    //User-wise rating
    List<Rating> getRatingsByUserId(String userId);

    //get All by Hotel
    List<Rating>  getRatingByHotelId(String hotelId);
}
