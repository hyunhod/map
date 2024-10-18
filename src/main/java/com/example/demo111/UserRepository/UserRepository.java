package com.example.demo111.UserRepository;

import com.example.demo111.UserDomain.NUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<NUser,Long> {
    NUser findByUsername(String username);

    Optional<NUser> findByEmail(String email);

    Optional<NUser> findByUsernameAndEmail(String username, String email);

}
