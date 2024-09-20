package com.example.demo111.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/rankings")
    public String showRankingsPage() {

        return "rankings";
    }
}
