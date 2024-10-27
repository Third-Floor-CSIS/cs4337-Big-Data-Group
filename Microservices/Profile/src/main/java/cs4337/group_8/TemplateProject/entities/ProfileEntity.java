package cs4337.group_8.TemplateProject.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;


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
    @Id // Primary key
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment in the database
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

//    // modification date?
//    // Insertable = false means that the column is not included in the insert statement
//    @Column(name = "signed_up", insertable = false)
//    private LocalDateTime signUpTime;
}
