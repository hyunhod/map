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

    public NComment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
