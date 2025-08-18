package com.hotel.hotelbooking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.hotelbooking.model.User;
import com.hotel.hotelbooking.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String listUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getAllUsers(pageable);

        model.addAttribute("userList", userPage);
        return "user/list";
    }

    @GetMapping("/detail/{id}")
    public String userDetail(@PathVariable int id, Model model) {
        Optional<User> roomOpt = userService.getUserById(id);
        if (roomOpt.isPresent()) {
            model.addAttribute("user", roomOpt.get());
            return "user/detail";
        }
        return "redirect:/user/list";
    }

}
