package com.hotel.hotelbooking.controller;

import com.hotel.hotelbooking.model.Booking;
import com.hotel.hotelbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBooking(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookingList", bookings);
        return "booking/list";

    }

    @GetMapping("/create")
    public String createBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "booking/create"; // hiển thị form tạo booking
    }

    @PostMapping("/create")
    public String saveBooking(@ModelAttribute("booking") Booking booking) {
        bookingService.saveBooking(booking);
        return "redirect:/booking";
    }

    @GetMapping("/edit/{id}")
    public String editBookingForm(@PathVariable int id, Model model) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            model.addAttribute("booking", bookingOpt.get());
            return "booking/edit";
        }
        return "redirect:/booking";
    }

    @PostMapping("/edit/{id}")
    public String editBooking(@PathVariable int id, @ModelAttribute("booking") Booking booking) {
        booking.setBookingId(id);
        bookingService.saveBooking(booking);
        return "redirect:/booking";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);
        return "redirect:/booking";
    }
}
