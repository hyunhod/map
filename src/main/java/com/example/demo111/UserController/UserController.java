package com.example.demo111.UserController;

import com.example.demo111.UserDomain.NUser;
import com.example.demo111.UserService.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "/Login/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "/Login/register"; // 회원가입 페이지 (HTML 템플릿 이름)
    }


    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {

        // 아이디 중복 체크
        if (userService.existsByUsername(username)) {
            model.addAttribute("errorMessage", "사용 중인 아이디입니다. 다른 아이디를 선택하세요.");
            return "register"; // 에러 메시지와 함께 회원가입 페이지로 리턴
        }


        NUser newUser = new NUser();
        newUser.setUsername(username);
        newUser.setPassword(password); // 나중에 패스워드 암호화 처리 필요
        newUser.setEmail(email);

        userService.saveUser(newUser); // 회원 정보 저장
        return "redirect:/login"; // 회원가입 후 로그인 페이지로 리다이렉트
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
            return "redirect:/Login/login?error";
        }

    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화 (사용자 정보 삭제)
        return "redirect:/login"; // 로그아웃 후 로그인 페이지로 이동
    }

    @GetMapping("/findUsername")
    public String showFindUsernamePage() {
        return "/Login/findUsername";
    }

    @PostMapping("/findUsername")
    public String findUsername(@RequestParam("email") String email, Model model) {
        String username = userService.findUsernameByEmail(email);

        if (username != null) {
            model.addAttribute("username", username);
            return "/Login/foundUsername"; // 아이디가 발견되면 결과 페이지로 이동
        } else {
            model.addAttribute("error", "이메일로 등록된 아이디를 찾을 수 없습니다.");
            return "/Login/findUsername";
        }
    }

    @GetMapping("/findPassword")
    public String showFindPasswordPage() {
        return "/Login/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("newPassword") String newPassword) {
        // 서비스에서 사용자 확인
        boolean isUserValid = userService.validateUser(username, email);
        if (isUserValid) {
            // 유저가 맞으면 비밀번호 업데이트
            userService.updatePassword(username, newPassword);
            return "redirect:/login?passwordResetSuccess"; // 성공 시 로그인 페이지로 이동
        } else {
            // 실패 시 다시 찾기 페이지로 리다이렉트
            return "redirect:/findPassword?error";
        }
    }




}
