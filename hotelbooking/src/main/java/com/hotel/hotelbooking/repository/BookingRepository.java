package com.hotel.hotelbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Booking;
import com.hotel.hotelbooking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByStatus(BookingStatus status);
}
