package cs4337.group_8.ProfileService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

/**
 * The @Data annotation is the same as the following annotations.
 * Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode, Value
 * Other annotations: @AllArgsConstructor, @NoArgsConstructor
 *
 */

@Entity
@Data
@ToString(callSuper = true, includeFieldNames = true)
@Table(name = "profile")
public class ProfileEntity {
    // Primary key
    @Id
    @Column
    private String userID;

    @Column
    private String fullName;

    @Column
    private String bio;

    @Column
    private String profilePic;

    @Column
    private int countFollower;

    @Column
    private int countFollowing;
}
