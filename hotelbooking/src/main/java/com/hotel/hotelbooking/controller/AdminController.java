package com.hotel.hotelbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.RoomStatus;
import com.hotel.hotelbooking.repository.BookingRepository;
import com.hotel.hotelbooking.repository.RoomRepository;
import com.hotel.hotelbooking.service.HotelManagementService;
import com.hotel.hotelbooking.service.RoomService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HotelManagementService hotelManagementService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/dashboard")
    public String listRoom(Model model) {
        return "admin/dashboard";
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        model.addAttribute("availableRooms", roomRepository.findByStatus(RoomStatus.AVAILABLE));
        return "admin/bookings";
    }

    @PostMapping("/confirm")
    public String confirmBooking(@RequestParam int bookingId,
            @RequestParam int roomId) {
        hotelManagementService.confirmBooking(bookingId, roomId);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/checkin")
    public String checkIn(@RequestParam int bookingId) {
        hotelManagementService.checkIn(bookingId);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestParam int bookingId) {
        hotelManagementService.checkOut(bookingId);
        return "redirect:/admin/bookings";
    }

}
