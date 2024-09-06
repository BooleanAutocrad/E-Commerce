package ideas.Ecommerce.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOnly {
    private Integer userId;
    private String userName;
    private String emailId;
    private String password;
    private String address;
}
