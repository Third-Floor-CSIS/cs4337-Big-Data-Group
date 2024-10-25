package cs4337.group_8.AuthenticationMicroservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    private String username;
    private String password;
    private String email;
    private String full_name;
    private String bio;

    @Column(name = "profile_pic")
    private String profile_picture;
    private Integer follower_count;
    private Integer following_count;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp created_at;
}
