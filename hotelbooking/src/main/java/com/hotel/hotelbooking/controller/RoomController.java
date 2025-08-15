package com.hotel.hotelbooking.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.service.AmenityService;
import com.hotel.hotelbooking.service.RoomImageService;
import com.hotel.hotelbooking.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AmenityService amenityService;

    @Autowired
    private RoomImageService roomImageService;

    @GetMapping("/list")
    public String listRoom(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Room> roomsPage = roomService.getAllRoom(pageable);

        model.addAttribute("roomList", roomsPage);
        return "room/list";
    }

    @GetMapping("/create")
    public String createRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("allAmenities", amenityService.getAllAmenities());
        return "room/create";
    }

    @PostMapping("/create")
    public String saveRoom(@ModelAttribute("room") Room room,
            @RequestParam("images") MultipartFile[] images,
            @RequestParam(name = "amenities", required = false) List<Integer> amenityIds) throws IOException {

        if (amenityIds != null) {
            room.setAmenities(amenityService.getAmenitiesByIds(amenityIds));
        }
        room.setIsActive(true);

        Room savedRoom = roomService.saveRoom(room);

        for (int i = 0; i < images.length; i++) {
            boolean isAvatar = (i == 0);
            roomImageService.saveImage(images[i], savedRoom, isAvatar);
        }

        return "redirect:/room/list";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable int id, Model model) {
        Optional<Room> roomOpt = roomService.getRoomById(id);
        if (roomOpt.isPresent()) {
            model.addAttribute("room", roomOpt.get());
            return "room/edit";
        }
        return "redirect:/room/list";
    }

    @PostMapping("/edit/{id}")
    public String editRoom(@PathVariable int id, @ModelAttribute("room") Room room) {
        room.setRoomId(id);
        roomService.saveRoom(room);
        return "redirect:/room/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
        return "redirect:/room/list";
    }

    @GetMapping("/roomlistpage")
    public String roomListPage(Model model) {
        List<Room> rooms = roomService.getAllRoom();
        model.addAttribute("roomList", rooms);
        return "room/roomlistpage";
    }

    @GetMapping("/detail/{id}")
    public String roomDetail(@PathVariable int id, Model model) {
        Optional<Room> roomOpt = roomService.getRoomById(id);

        if (roomOpt.isEmpty()) {
            return "redirect:/room/list"; // Nếu không tìm thấy phòng thì quay về danh sách
        }

        model.addAttribute("room", roomOpt.get());
        return "room/detail"; // đường dẫn tới file templates/room/detail.html
    }

}
