package com.hotel.hotelbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.hotel.hotelbooking.model.Booking;
import com.hotel.hotelbooking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByStatus(BookingStatus status);
    Page<Booking> findByUserUserId(int userId, Pageable pageable);
    Page<Booking> findByUserUserIdAndStatus(int userId, BookingStatus status, Pageable pageable);
}
