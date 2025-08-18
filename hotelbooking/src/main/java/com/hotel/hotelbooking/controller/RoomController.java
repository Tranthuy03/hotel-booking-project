package com.hotel.hotelbooking.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotel.hotelbooking.model.Amenity;
import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.RoomImage;
import com.hotel.hotelbooking.model.RoomStatus;
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
        room.setStatus(RoomStatus.AVAILABLE);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        Room savedRoom = roomService.saveRoom(room);

        for (int i = 0; i < images.length; i++) {
            boolean isAvatar = (i == 0);
            roomImageService.saveImage(images[i], savedRoom, isAvatar);
        }
        System.out.println("Số file upload: " + images.length);
        for (MultipartFile file : images) {
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File empty? " + file.isEmpty());
        }
        return "redirect:/room/list";
    }

    @GetMapping("/room/edit/{id}")
    public String editRoom(@PathVariable("id") int id, Model model) {
        Room room = roomService.getRoomById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        model.addAttribute("room", room);
        model.addAttribute("allAmenities", amenityService.getAllAmenities());
        model.addAttribute("roomId", room.getRoomId());
        return "room/edit";
    }

    @PostMapping("/room/update/{id}")
    public String updateRoom(
            @PathVariable("id") int id,
            @ModelAttribute("room") Room roomForm,
            @RequestParam(value = "amenities", required = false) List<Integer> amenityIds,
            @RequestParam(value = "images", required = false) List<MultipartFile> files,
            RedirectAttributes redirectAttributes) {

        try {
            Room room = roomService.getRoomById(id)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            // Cập nhật các field cơ bản
            room.setRoomName(roomForm.getRoomName());
            room.setRoomNumber(roomForm.getRoomNumber());
            room.setRoomType(roomForm.getRoomType());
            room.setNumberOfBed(roomForm.getNumberOfBed());
            room.setViewType(roomForm.getViewType());
            room.setDescription(roomForm.getDescription());
            room.setPrice(roomForm.getPrice());
            room.setCapacity(roomForm.getCapacity());
            room.setUpdatedAt(LocalDateTime.now());

            // Amenities
            if (amenityIds != null) {
                List<Amenity> amenities = amenityService.getAmenitiesByIds(amenityIds);
                room.setAmenities(amenities);
            } else {
                room.setAmenities(new ArrayList<>());
            }

            // Upload ảnh mới (nếu có)
            if (files != null) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        Path path = Paths.get("uploads/rooms/" + fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, file.getBytes());

                        RoomImage img = new RoomImage();
                        img.setImageUrl(fileName);
                        img.setRoom(room);
                        room.getImagesList().add(img);
                    }
                }
            }

            roomService.saveRoom(room);
            redirectAttributes.addFlashAttribute("success", "Room updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Update failed: " + e.getMessage());
        }
        return "redirect:/room";
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
