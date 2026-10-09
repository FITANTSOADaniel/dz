package mgit.supplychain.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalUsers {
    private Long id;
    private String email;
    private String last_name;
    private String first_name;
    private String bio;
}
