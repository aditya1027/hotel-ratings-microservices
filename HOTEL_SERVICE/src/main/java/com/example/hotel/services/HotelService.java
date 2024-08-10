package com.example.hotel.services;

import com.example.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    Hotel create(Hotel hotel);

        List<Hotel> getAllHotels();

        Hotel get(String id);
}
