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
// Example toString
@Entity
@Data
@ToString(callSuper = true, includeFieldNames = true)
@Table(name = "name_of_table")
public class SampleEntity {
    // Primary key
    @Id
    // Auto-increment in the database
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    // If the column name is the same as the field name, you can omit the name attribute
    @Column
    private String firstname;

    // Or you can use the name of the column in the table
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // Insertable = false means that the column is not included in the insert statement
    @Column(name = "signed_up", insertable = false)
    private LocalDateTime signUpTime;

    @Column
    @ToString.Exclude private boolean verified;
}
