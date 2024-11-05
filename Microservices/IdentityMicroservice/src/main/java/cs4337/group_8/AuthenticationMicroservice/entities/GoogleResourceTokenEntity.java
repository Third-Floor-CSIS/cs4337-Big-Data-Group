package cs4337.group_8.AuthenticationMicroservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tokens")
public class GoogleResourceTokenEntity {
    @Id
    private Integer userId;
    private String refreshToken;
    private String currentAccessToken;
    private Instant expirationTimeAccessToken;
    private Instant expirationTimeRefreshToken;
}
