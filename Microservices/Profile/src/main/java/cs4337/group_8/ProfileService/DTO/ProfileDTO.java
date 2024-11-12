package cs4337.group_8.ProfileService.DTO;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDTO {
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
