package com.example.demo111.UserService;

import com.example.demo111.UserDomain.NUser;
import com.example.demo111.UserRepository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 저장
    public void saveUser(NUser user) {
        userRepository.save(user);
    }

    // 아이디 중복 체크
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // 로그인 검증
    public NUser authenticate(String username, String password) {
        // 데이터베이스에서 사용자 정보 확인
        NUser NUser = userRepository.findByUsername(username);
        if (NUser != null && NUser.getPassword().equals(password)) {
            return NUser; // 비밀번호 일치 시 인증 성공
        }
        return null; // 인증 실패
    }


    public String findUsernameByEmail(String email) {
        Optional<NUser> user = userRepository.findByEmail(email);
        return user.map(NUser::getUsername).orElse(null);
    }

    // 사용자 검증 (아이디와 이름이 일치하는지 확인)
    public boolean validateUser(String username, String email) {
        Optional<NUser> user = userRepository.findByUsernameAndEmail(username, email);
        return user.isPresent();
    }

    // 비밀번호 업데이트
    public void updatePassword(String username, String newPassword) {
        NUser user = userRepository.findByUsername(username);
        user.setPassword(newPassword); // 비밀번호는 실제로 암호화하는 게 좋습니다 (예: BCrypt)
        userRepository.save(user);
    }

}
