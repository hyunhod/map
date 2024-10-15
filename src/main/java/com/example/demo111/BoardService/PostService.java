package com.example.demo111.BoardService;

import com.example.demo111.BoardDomain.Post;
import com.example.demo111.BoardRepository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
    }

    public void save(Post post) {
        postRepository.save(post);
    }
}
