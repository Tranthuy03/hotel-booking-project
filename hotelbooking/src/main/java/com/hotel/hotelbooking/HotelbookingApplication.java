package com.hotel.hotelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelbookingApplication {

    public static void main(String[] args) {
        System.out.println("user.dir = " + System.getProperty("user.dir"));
        SpringApplication.run(HotelbookingApplication.class, args);
    }

}
