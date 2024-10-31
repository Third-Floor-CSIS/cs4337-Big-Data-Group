import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.posts.entity.Post;
import com.example.posts.dto.PostDTO;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post post);
    Post toEntity(PostDTO postDTO);
}
