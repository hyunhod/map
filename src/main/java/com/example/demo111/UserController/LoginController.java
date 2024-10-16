package com.example.demo111.UserController;

import com.example.demo111.UserDomain.NUser;
import com.example.demo111.UserService.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("first")
    public String login(){
        return "/Login/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // 로그인 검증 로직
        NUser NUser = userService.authenticate(username, password);
        if (NUser != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("username", NUser.getUsername());
            return "redirect:/board";
        } else {
            return "redirect:/login?error";
        }
    }
}
