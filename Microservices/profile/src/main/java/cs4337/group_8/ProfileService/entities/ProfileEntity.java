package cs4337.group_8.ProfileService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;


/**
 * The @Data annotation is the same as the following annotations:
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
    private String user_id;

    @Column
    private String full_name;

    @Column
    private String bio;

    @Column
    private String profile_pic;

    @Column
    private int count_follower;

    @Column
    private int count_following;
}
