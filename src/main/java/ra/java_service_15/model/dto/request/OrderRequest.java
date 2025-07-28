package ra.java_service_15.model.dto.request;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private List<OrderItemRequest> items;
}