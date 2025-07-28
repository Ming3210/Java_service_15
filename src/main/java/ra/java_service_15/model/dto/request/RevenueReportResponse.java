package ra.java_service_15.model.dto.response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueReportResponse {

    private String reportType; // "order", "user", "product"
    private Double totalRevenue;
    private List<RevenueData> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RevenueData {
        private String label;
        private String description;
        private Double revenue;
        private Long quantity;
        private Long count;
    }
}