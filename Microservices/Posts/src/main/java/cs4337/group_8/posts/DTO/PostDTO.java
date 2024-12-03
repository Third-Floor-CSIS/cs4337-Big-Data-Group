package cs4337.group_8.posts.DTO;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private Long userId;
    private String picUrl;
    private int likesCountDTO;



}
