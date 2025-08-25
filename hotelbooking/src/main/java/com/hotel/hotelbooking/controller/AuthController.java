package com.hotel.hotelbooking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotel.hotelbooking.model.Role;
import com.hotel.hotelbooking.model.User;
import com.hotel.hotelbooking.repository.RoleRepository;
import com.hotel.hotelbooking.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    // Hiển thị form login
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, HttpSession session, Model model) {
        Optional<User> dbUser = userService.findByEmail(user.getEmail());

        if (dbUser.isPresent() && dbUser.get().getPassword().equals(user.getPassword())) {
            // Lưu user vào session
            session.setAttribute("loggedUser", dbUser.get());
            return "redirect:/"; // sau khi login thành công, về trang chủ
        } else {
            model.addAttribute("error", "Invalid email or password!");
            return "auth/login";
        }
    }

    // Hiển thị form register
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Xử lý đăng ký (gộp logic 2 method lại)
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "auth/register";
        }

        // Gán role mặc định cho client (id=2)
        Role clientRole = roleRepository.findById(2).orElse(new Role(2, "CLIENT"));
        user.setRole(clientRole);

        userService.saveUser(user);
        return "redirect:/auth/login?registered";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
