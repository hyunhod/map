package com.example.demo111;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableScheduling
public class Demo111Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo111Application.class, args);
    }


}
