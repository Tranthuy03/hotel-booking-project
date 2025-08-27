package com.hotel.hotelbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByStatus(RoomStatus status);
    
}
