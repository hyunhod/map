package com.example.demo111.BoardRepository;

import com.example.demo111.BoardDomain.NComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NCommentRepository extends JpaRepository<NComment,Long> {
}
