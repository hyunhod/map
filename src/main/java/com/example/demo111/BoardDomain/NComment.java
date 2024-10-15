package com.example.demo111.BoardDomain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
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
}
