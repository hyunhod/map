package com.example.demo111.BoardController;

import com.example.demo111.BoardDomain.NComment;
import com.example.demo111.BoardDomain.Post;
import com.example.demo111.BoardService.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
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
    public String createPost(@ModelAttribute Post post, HttpSession session) {
        String username = (String)session.getAttribute("username");
        post.setAuthor(username);
        postService.save(post); // 게시글 저장
        return "redirect:/board"; // 게시글 목록으로 리다이렉트
    }
    // 게시글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model,HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 로그인된 사용자가 없을 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        List<NComment> comments = post.getComments(); // 해당 게시글의 댓글 목록 가져오기
        model.addAttribute("comments", comments);
        return "Board/view"; // 게시글 상세 보기 페이지
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        Post post = postService.findById(id);

        // 세션에서 로그인한 사용자 정보 가져오기
        String loggedInUsername = (String) session.getAttribute("username");


        // 게시글 작성자와 로그인한 사용자가 동일한지 확인
        if (!post.getAuthor().equals(loggedInUsername)) {
            // 삭제 권한이 없을 경우 에러 페이지로 리다이렉트
            return "redirect:/error?unauthorized";
        }

        postService.deleteById(id); // 게시글 삭제
        return "redirect:/board";
    }



}
