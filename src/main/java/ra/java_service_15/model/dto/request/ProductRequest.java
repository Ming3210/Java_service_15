package ra.java_service_15.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {
    private Long id;
    private String productName;
    private String producer;
    private Integer yearMaking;
    private LocalDate expireDate;
    private Double price;
    private Integer quantity;
    private Long categoryId;
    private String categoryName;
}
