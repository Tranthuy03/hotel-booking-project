package com.hotel.hotelbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Amenity;

public interface AmenityRepository extends JpaRepository<Amenity, Integer> {

    List<Amenity> findByAmenityIdIn(List<Integer> ids);
}
