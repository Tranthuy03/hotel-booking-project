package com.hotel.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
