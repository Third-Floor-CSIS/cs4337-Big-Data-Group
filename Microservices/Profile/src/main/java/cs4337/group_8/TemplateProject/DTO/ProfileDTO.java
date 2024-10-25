package cs4337.group_8.TemplateProject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ProfileDTO {
// string as it should be alphanumeric
    private String user_id;
    private String full_name;
    private String bio;
    private String profile_pic;
    private int count_follower;
    private int count_following;

//    public ProfileDTO (String property1){
//        this.property1 = property1;
//    }
}
