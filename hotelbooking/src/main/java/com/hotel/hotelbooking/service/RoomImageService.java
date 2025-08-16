package com.hotel.hotelbooking.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    public RoomImage saveImage(MultipartFile file, Room room, boolean isAvatar) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Thư mục gốc lưu file
        String uploadDir = System.getProperty("user.dir") + "/hotelbooking/uploads/rooms/" + room.getRoomId();
        Path uploadPath = Paths.get(uploadDir);

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Lấy extension của file và tạo tên file mới an toàn
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + extension;

        // Lưu file vào thư mục
        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo entity RoomImage và lưu DB
        RoomImage roomImage = new RoomImage();
        roomImage.setImageUrl(newFileName); // chỉ lưu tên file
        roomImage.setAvatar(isAvatar);
        roomImage.setRoom(room);

        System.out.println("Upload path: " + filePath.toAbsolutePath());
        System.out.println("Saving file: " + newFileName);

        System.out.println("Upload path: " + uploadPath.toAbsolutePath());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("Thư mục được tạo: " + uploadPath.toAbsolutePath());
        }
        return roomImageRepository.save(roomImage);
    }
}
