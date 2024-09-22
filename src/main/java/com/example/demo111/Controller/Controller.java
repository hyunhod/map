package com.example.demo111.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/")
    public String showRankingsPage() {

        return "search";
    }
    @GetMapping("/lawd")
    public String showLawdPage(){
        return "lawdCode";
    }
}
