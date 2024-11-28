package cs4337.group_8.AuthenticationMicroservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;
    private String password;
    private String email;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant created_at;
}
