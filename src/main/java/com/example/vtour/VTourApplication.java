package com.example.vtour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class VTourApplication {

    public static void main(String[] args) {
        SpringApplication.run(VTourApplication.class, args);
    }

}
