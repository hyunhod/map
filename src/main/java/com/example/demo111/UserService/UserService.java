package com.example.demo111.UserService;

import com.example.demo111.UserDomain.NUser;
import com.example.demo111.UserRepository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 로그인 검증 로직
    public NUser authenticate(String username, String password) {
        // 데이터베이스에서 사용자 정보 확인
        NUser NUser = userRepository.findByUsername(username);
        if (NUser != null && NUser.getPassword().equals(password)) {
            return NUser; // 비밀번호 일치 시 인증 성공
        }
        return null; // 인증 실패
    }
}
