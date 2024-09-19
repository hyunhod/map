package com.example.demo111;

import com.example.demo111.Dto.ResponseDto;
import com.example.demo111.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Demo111Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo111Application.class, args);
    }

    }
