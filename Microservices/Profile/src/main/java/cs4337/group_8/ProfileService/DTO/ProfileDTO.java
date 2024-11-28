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
    private String userID;

    @Size(
        min = 1,
        message = "Fullname is too short"
    )
    @NotNull
    private String fullName;

    // does not have to be filled in
    @NotNull
    private String bio;

    @NotNull
    private String profilePic;

    @PositiveOrZero
    @NotNull
    private int countFollower;

    @PositiveOrZero
    @NotNull
    private int countFollowing;
}
