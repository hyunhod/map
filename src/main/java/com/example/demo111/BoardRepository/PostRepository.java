package com.example.demo111.BoardRepository;

import com.example.demo111.BoardDomain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
