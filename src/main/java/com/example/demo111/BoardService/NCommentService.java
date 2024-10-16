package com.example.demo111.BoardService;
import com.example.demo111.BoardDomain.NComment;
import com.example.demo111.BoardRepository.NCommentRepository;
import org.springframework.stereotype.Service;


@Service
public class NCommentService {
    private final NCommentRepository commentRepository;

    public NCommentService(NCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public void save(NComment comment){
        commentRepository.save(comment);
    }
}
