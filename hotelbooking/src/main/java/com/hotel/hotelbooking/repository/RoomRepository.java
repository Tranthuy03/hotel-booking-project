package com.hotel.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotel.hotelbooking.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
