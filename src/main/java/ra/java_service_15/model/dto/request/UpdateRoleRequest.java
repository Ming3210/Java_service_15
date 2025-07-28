package ra.java_service_15.model.dto.request;


import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {

    @NotBlank(message = "Role không được để trống")
    @Pattern(regexp = "^(ADMIN|STAFF|CUSTOMER)$",
            message = "Role chỉ được phép là: ADMIN, STAFF, CUSTOMER")
    private String roleName;
}
