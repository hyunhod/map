package com.example.demo111.BoardController;

import com.example.demo111.BoardDomain.NComment;
import com.example.demo111.BoardDomain.Post;
import com.example.demo111.BoardService.NCommentService;
import com.example.demo111.BoardService.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final NCommentService commentService;
    private final PostService postService;

    public CommentController(NCommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    // 댓글 저장
    @PostMapping
    public String saveComment(@RequestParam Long postId, @RequestParam String content, @RequestParam String author) {
        System.out.println("jinib");
        Post post = postService.findById(postId); // 댓글이 달릴 게시글 찾기
        NComment comment = new NComment();
        comment.setPost(post); // 댓글과 게시글 연결
        comment.setContent(content);
        comment.setAuthor(author);
        commentService.save(comment); // 댓글 저장
        return "redirect:/board/" + postId; // 댓글 작성 후 해당 게시글로 리다이렉트
    }
}
