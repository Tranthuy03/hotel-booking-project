package com.hotel.hotelbooking.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.hotelbooking.model.Booking;
import com.hotel.hotelbooking.model.BookingStatus;
import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.RoomStatus;
import com.hotel.hotelbooking.model.User;
import com.hotel.hotelbooking.repository.BookingRepository;
import com.hotel.hotelbooking.repository.RoomRepository;

@Service
public class HotelManagementService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Booking createBooking(Room room, User user, LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = days * room.getPrice();

        Booking booking = new Booking();
        booking.setRoom(null); // chưa gán phòng cụ thể
        booking.setUser(user);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    // Bước 2: Xác nhận và gán phòng
    public Booking confirmBooking(Integer bookingId, Integer roomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RuntimeException("Room not available");
        }

        booking.setRoom(room);
        booking.setStatus(BookingStatus.CONFIRMED);

        room.setStatus(RoomStatus.RESERVED);

        roomRepository.save(room);
        return bookingRepository.save(booking);
    }

    // Bước 3: Check-in
    public Booking checkIn(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not confirmed");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.getRoom().setStatus(RoomStatus.OCCUPIED);

        roomRepository.save(booking.getRoom());
        return bookingRepository.save(booking);
    }

    // Bước 4: Check-out
    public Booking checkOut(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new RuntimeException("Booking is not checked in");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        booking.getRoom().setStatus(RoomStatus.AVAILABLE);

        roomRepository.save(booking.getRoom());
        return bookingRepository.save(booking);
    }
}
