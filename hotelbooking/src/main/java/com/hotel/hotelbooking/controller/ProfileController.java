package com.hotel.hotelbooking.controller;

import com.hotel.hotelbooking.model.User;
import com.hotel.hotelbooking.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // Hiển thị trang profile
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/auth/login";
        }

        // Lấy lại thông tin từ DB (tránh session cũ)
        User user = userService.getUserById(loggedUser.getUserId()).orElse(loggedUser);

        model.addAttribute("user", user);
        return "profile/user";
    }

    // Cập nhật profile
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User formUser, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/auth/login";
        }

        // Lấy user từ DB để tránh mất dữ liệu không có trong form (password, role, createdAt,...)
        User dbUser = userService.getUserById(loggedUser.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Cập nhật thông tin từ form
        dbUser.setFirstName(formUser.getFirstName());
        dbUser.setLastName(formUser.getLastName());
        dbUser.setAddress(formUser.getAddress());
        dbUser.setPhone(formUser.getPhone());
        dbUser.setUpdatedAt(LocalDateTime.now());

        // Lưu lại
        User updatedUser = userService.saveUser(dbUser);

        // Cập nhật session
        session.setAttribute("loggedUser", updatedUser);

        return "redirect:/profile";
    }
}
