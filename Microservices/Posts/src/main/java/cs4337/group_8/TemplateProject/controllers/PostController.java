import com.example.posts.dto.PostDTO;
import com.example.posts.entity.Post;
import com.example.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        Post post = new Post(postDTO.getUserId(), postDTO.getContent());
        return convertToDTO(postService.createPost(post));
    }

    @GetMapping("/user/{userId}")
    public List<PostDTO> getUserPosts(@PathVariable Long userId) {
        return postService.getPostsByUser(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok(convertToDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setLikesCount(post.getLikesCount());
        return dto;
    }

    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        return postMapper.toDTO(postService.createPost(post));
    }
}
