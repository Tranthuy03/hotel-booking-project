package com.hotel.hotelbooking.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotel.hotelbooking.model.Room;
import com.hotel.hotelbooking.model.RoomImage;
import com.hotel.hotelbooking.repository.RoomImageRepository;

@Service
public class RoomImageService {

    @Autowired
    private RoomImageRepository roomImageRepository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public RoomImage saveImage(MultipartFile file, Room room, boolean isAvatar) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Tạo thư mục lưu file theo roomId
        String subFolder = "rooms/" + room.getRoomId();
        Path uploadPath = Paths.get(UPLOAD_DIR, subFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lưu file vào thư mục
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo entity RoomImage và lưu DB
        RoomImage roomImage = new RoomImage();
        roomImage.setImageUrl("/uploads/" + subFolder + "/" + fileName);
        roomImage.setAvatar(isAvatar);
        roomImage.setRoom(room);

        return roomImageRepository.save(roomImage);
    }
}
