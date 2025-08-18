package com.hotel.hotelbooking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.hotelbooking.model.Booking;
import com.hotel.hotelbooking.service.BookingService;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/list")
    public String listBooking(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> roomsPage = bookingService.getAllBookings(pageable);

        model.addAttribute("bookingList", roomsPage);
        return "booking/list";
    }

    @GetMapping("/create")
    public String createBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "booking/create";
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
