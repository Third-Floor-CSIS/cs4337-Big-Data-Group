package cs4337.group_8.posts.controllers;

import cs4337.group_8.posts.DTO.PostDTO;
import cs4337.group_8.posts.entities.Post;
import cs4337.group_8.posts.services.JwtService;
import cs4337.group_8.posts.services.PostService;
import cs4337.group_8.posts.mappers.PostMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostMapper postMapper;
    private PostService postService;
    private final JwtService jwtService;


    public PostController(PostService postService, JwtService jwtService, PostMapper postMapper) {
        this.postService = postService;
        this.jwtService = jwtService;
        this.postMapper = postMapper;
    }

    @PostMapping
    public PostDTO createPost(
            @RequestBody PostDTO postDTO,
            @RequestHeader(name="Authorization") String jwtToken
    ) {
        Post post = postMapper.toEntity(postDTO);
        return postMapper.toDTO(postService.createPost(post, jwtToken));
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping("/user/{userId}")
    public List<PostDTO> getUserPosts(@PathVariable Long userId) {
        return postService.getPostsByUser(userId).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok(postMapper.toDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }
}
