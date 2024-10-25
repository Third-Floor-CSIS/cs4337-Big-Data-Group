package cs4337.group_8.AuthenticationMicroservice.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SampleDTO {
    private String property1;
    private String property2;
    // You can have hashmaps (as json objects) and lists (as json arrays) as properties
    private Map<String, Object> property3;
    private List<String> property4;

    public SampleDTO(String property1){
        this.property1 = property1;
    }
}
