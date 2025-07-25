package com.hotel.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotelbooking.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
