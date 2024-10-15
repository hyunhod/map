package com.example.demo111.BoardController;

import com.example.demo111.BoardDomain.NComment;
import com.example.demo111.BoardDomain.Post;
import com.example.demo111.BoardService.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final PostService postService;

    public BoardController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 보기
    @GetMapping
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "/Board/communication";
    }

    @GetMapping("/write")
    public String write(){
        return "/Board/write";
    }

    // 게시글 작성 후 저장
    @PostMapping("/save")
    public String createPost(@ModelAttribute Post post) {
        postService.save(post); // 게시글 저장
        return "redirect:/board"; // 게시글 목록으로 리다이렉트
    }
    // 게시글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        List<NComment> comments = post.getComments(); // 해당 게시글의 댓글 목록 가져오기
        model.addAttribute("comments", comments);
        return "Board/view"; // 게시글 상세 보기 페이지
    }


}
