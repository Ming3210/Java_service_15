package ra.java_service_15.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister {
    private String username;
    private String fullName;
    private String password;
    private String address;
    private String email;
    private String phone;
    private boolean enabled;
    private List<String> roles;
}
