package com.example.demo111.BoardController;

import com.example.demo111.BoardDomain.NComment;
import com.example.demo111.BoardDomain.Post;
import com.example.demo111.BoardService.NCommentService;
import com.example.demo111.BoardService.PostService;
import jakarta.servlet.http.HttpSession;
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
    public String saveComment(@RequestParam Long postId, @RequestParam String content, HttpSession session) {

        // 세션에서 로그인한 사용자 정보 가져오기
        String username = (String) session.getAttribute("username");

        if (username == null) {
            // 로그인이 안 된 상태라면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        Post post = postService.findById(postId); // 댓글이 달릴 게시글 찾기
        NComment comment = new NComment();
        comment.setPost(post); // 댓글과 게시글 연결
        comment.setContent(content);
        comment.setAuthor(username);
        commentService.save(comment); // 댓글 저장
        return "redirect:/board/" + postId; // 댓글 작성 후 해당 게시글로 리다이렉트
    }

}
