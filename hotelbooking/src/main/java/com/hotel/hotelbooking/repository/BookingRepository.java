package com.hotel.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
