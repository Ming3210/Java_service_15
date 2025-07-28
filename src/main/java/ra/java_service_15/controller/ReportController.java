package ra.java_service_15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ra.java_service_15.model.dto.response.ErrorResponse;
import ra.java_service_15.model.dto.response.RevenueReportResponse;
import ra.java_service_15.service.ReportService;

import java.security.Principal;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueReport(
            @RequestParam(defaultValue = "order") String type,
            Principal principal) {
        try {
            RevenueReportResponse report = reportService.getRevenueReport(type);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Lỗi khi tạo báo cáo: " + e.getMessage()));
        }
    }


    @GetMapping("/summary")
    public ResponseEntity<?> getRevenueSummary(Principal principal) {
        try {
            // Lấy báo cáo tổng hợp
            RevenueReportResponse orderReport = reportService.getRevenueReport("order");
            RevenueReportResponse userReport = reportService.getRevenueReport("user");
            RevenueReportResponse productReport = reportService.getRevenueReport("product");

            RevenueSummary summary = RevenueSummary.builder()
                    .totalRevenue(orderReport.getTotalRevenue())
                    .totalOrders((long) orderReport.getData().size())
                    .totalCustomers((long) userReport.getData().size())
                    .totalProducts((long) productReport.getData().size())
                    .topCustomer(userReport.getData().isEmpty() ? null :
                            userReport.getData().get(0).getLabel())
                    .topProduct(productReport.getData().isEmpty() ? null :
                            productReport.getData().get(0).getLabel())
                    .build();

            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Lỗi khi tạo tổng quan: " + e.getMessage()));
        }
    }




    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class RevenueSummary {
        private Double totalRevenue;
        private Long totalOrders;
        private Long totalCustomers;
        private Long totalProducts;
        private String topCustomer;
        private String topProduct;
    }
}