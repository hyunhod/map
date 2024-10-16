package com.example.demo111.BoardDomain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Data
public class NComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 입력 및 출력 시 적용되는 포맷
    private LocalDateTime createdDate;

}
