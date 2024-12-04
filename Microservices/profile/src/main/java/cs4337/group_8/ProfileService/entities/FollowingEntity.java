package cs4337.group_8.ProfileService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(FollowingId.class) // Composite key handler
@Table(name = "following")
public class FollowingEntity {
    @Id
    @Column(name = "initiator_id", nullable = false)
    private String initiatorId;

    @Id
    @Column(name = "target_id", nullable = false)
    private String targetId;

    @Column(name = "time", nullable = false)
    private Instant time;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
