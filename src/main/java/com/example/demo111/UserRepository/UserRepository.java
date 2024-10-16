package com.example.demo111.UserRepository;

import com.example.demo111.UserDomain.NUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<NUser,Long> {
    NUser findByUsername(String username);
}
