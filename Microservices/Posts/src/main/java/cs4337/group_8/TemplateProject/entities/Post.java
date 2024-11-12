package cs4337.group_8.TemplateProject.entities;

//import javax.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    private String caption;
    private String picUrl;
    private LocalDateTime createdAt;
    private long userId; // to track which user created the post

    private int likesCount = 0; // to track number of likes

    public Post(Long id, String content) {

        this.id = id;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }


}