package cs4337.group_8.TemplateProject.DTO;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class ProfileDTOValidated {
    @NotNull
    private String user_id;

    @Size(
        min = 1,
        message = "Fullname is too short"
    )
    @NotNull
    private String full_name;

    // does not have to be filled in
    @NotNull
    private String bio;

    @NotNull
    private String profile_pic;

    @PositiveOrZero
    @NotNull
    private int count_follower;

    @PositiveOrZero
    @NotNull
    private int count_following;
}
