package cs4337.group_8.AuthenticationMicroservice.entities;

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
// Example toString
@Entity
@Data
@ToString(callSuper = true, includeFieldNames = true)
@Table(name = "name_of_table")
public class SampleEntity {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment in the database
    @Column
    private Integer id;

    @Column // If the column name is the same as the field name, you can omit the name attribute
    private String firstname;

    @Column(name = "date_of_birth") // Or you can use the name of the column in the table
    private Date dateOfBirth;

    @Column(name = "signed_up", insertable = false) // Insertable = false means that the column is not included in the insert statement
    private LocalDateTime signUpTime;

    @Column
    @ToString.Exclude private boolean verified;
}
