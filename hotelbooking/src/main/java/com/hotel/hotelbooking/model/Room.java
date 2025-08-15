package com.hotel.hotelbooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    private String roomName;
    private String roomNumber;
    private String roomType;
    private Integer numberOfBed;
    private String viewType;
    private String description;
    private Double price;
    private Integer capacity;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToMany
    @JoinTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private List<Amenity> amenities = new ArrayList<>();

}
