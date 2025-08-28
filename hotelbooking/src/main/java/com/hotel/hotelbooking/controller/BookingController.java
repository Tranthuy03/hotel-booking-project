package com.hotel.hotelbooking.controller;

import java.time.LocalDateTime;
import java.util.List;
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
import com.hotel.hotelbooking.model.BookingStatus;
import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.User;
import com.hotel.hotelbooking.service.BookingService;
import com.hotel.hotelbooking.service.RoomService;
import com.hotel.hotelbooking.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    public String listBooking(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> roomsPage = bookingService.getAllBookings(pageable);

        model.addAttribute("bookingList", roomsPage);
        return "booking/list";
    }

    @GetMapping("/HistoryUserBooking")
    public String listUserBooking(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";  // chưa login
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Booking> bookingsPage;

        if (status != null && !status.isEmpty()) {   //lọc theo trạng thái
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            bookingsPage = bookingService.findByUserAndStatus(user.getUserId(), bookingStatus, PageRequest.of(page, size));
        } else {
            bookingsPage = bookingService.findByUser(user.getUserId(), PageRequest.of(page, size));
        }

        model.addAttribute("bookingList", bookingsPage);
        model.addAttribute("search", search);
        model.addAttribute("statusFilter", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingsPage.getTotalPages());

        return "booking/HistoryUserBooking";
    }

    @GetMapping("/detail/{id}")
    public String bookingDetail(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login"; // Chưa login thì chuyển về login
        }

        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Chỉ cho xem booking của chính mình
        if (booking.getUser().getUserId() != user.getUserId()) {
            return "redirect:/booking/HistoryUserBooking";
        }

        model.addAttribute("booking", booking);
        return "booking/detail";
    }

    @GetMapping("/create")
    public String createBookingForm(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/auth/login"; // chưa login
        }
        List<Room> rooms = roomService.getAllRoom();
        model.addAttribute("roomList", rooms);
        model.addAttribute("booking", new Booking());
        return "booking/create";
    }

    @PostMapping("/create")
    public String saveBooking(@ModelAttribute("booking") Booking booking, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/auth/login";
        }

        booking.setUser(loggedUser);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingService.saveBooking(booking);
        return "redirect:/room/roomlistpage";
    }

    @GetMapping("/edit/{id}")
    public String editBookingForm(@PathVariable int id, Model model) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            model.addAttribute("booking", bookingOpt.get());
            return "booking/edit";
        }
        return "redirect:/booking/list";
    }

    @PostMapping("/edit/{id}")
    public String editBooking(@PathVariable int id, @ModelAttribute("booking") Booking booking) {
        booking.setBookingId(id);
        bookingService.saveBooking(booking);
        return "redirect:/booking/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);
        return "redirect:/booking/list";
    }
}
