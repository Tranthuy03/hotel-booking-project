package com.hotel.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {

}
