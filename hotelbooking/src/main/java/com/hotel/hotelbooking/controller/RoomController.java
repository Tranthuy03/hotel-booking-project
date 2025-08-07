package com.hotel.hotelbooking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String listRoom(Model model) {
        List<Room> rooms = roomService.getAllRoom();
        model.addAttribute("roomList", rooms);
        return "room/list";
    }

    @GetMapping("/create")
    public String createRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/create";
    }

    @PostMapping("/create")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/room";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable int id, Model model) {
        Optional<Room> roomOpt = roomService.getRoomById(id);
        if (roomOpt.isPresent()) {
            model.addAttribute("room", roomOpt.get());
            return "room/edit";
        }
        return "redirect:/room";
    }

    @PostMapping("/edit/{id}")
    public String editRoom(@PathVariable int id, @ModelAttribute("room") Room room) {
        room.setRoomId(id);
        roomService.saveRoom(room);
        return "redirect:/room";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
        return "redirect:/room";
    }

    @GetMapping("/roomlistpage")
    public String roomListPage(Model model) {
        List<Room> rooms = roomService.getAllRoom();
        model.addAttribute("roomList", rooms);
        return "room/roomlistpage";
    }

}
