package com.rigandbarter.pcbuilderservice;

import com.rigandbarter.eventlibrary.EnableRBEvents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRBEvents
@SpringBootApplication
public class PCBuilderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PCBuilderServiceApplication.class, args);
    }
}