package ra.java_service_15.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ra.java_service_15.model.dto.response.RevenueReportResponse;
import ra.java_service_15.repository.OrderRepository;
import ra.java_service_15.service.ReportService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public RevenueReportResponse getRevenueReport(String type) {
        // Tính tổng doanh thu
        Double totalRevenue = orderRepository.getTotalRevenue();
        if (totalRevenue == null) totalRevenue = 0.0;

        List<RevenueReportResponse.RevenueData> revenueDataList = new ArrayList<>();

        switch (type.toLowerCase()) {
            case "order":
                revenueDataList = getRevenueByOrder();
                break;
            case "user":
                revenueDataList = getRevenueByUser();
                break;
            case "product":
                revenueDataList = getRevenueByProduct();
                break;
            default:
                throw new IllegalArgumentException("Type chỉ được phép là: order, user, product");
        }

        return RevenueReportResponse.builder()
                .reportType(type)
                .totalRevenue(Math.round(totalRevenue * 100.0) / 100.0)
                .data(revenueDataList)
                .build();
    }

    private List<RevenueReportResponse.RevenueData> getRevenueByOrder() {
        List<Object[]> results = orderRepository.getRevenueByOrder();
        List<RevenueReportResponse.RevenueData> dataList = new ArrayList<>();

        for (Object[] result : results) {
            Long orderId = (Long) result[0];
            String username = (String) result[1];
            Double revenue = ((Number) result[2]).doubleValue();

            RevenueReportResponse.RevenueData data = RevenueReportResponse.RevenueData.builder()
                    .label("Order #" + orderId)
                    .description("Customer: " + username)
                    .revenue(Math.round(revenue * 100.0) / 100.0)
                    .build();

            dataList.add(data);
        }

        return dataList;
    }

    private List<RevenueReportResponse.RevenueData> getRevenueByUser() {
        List<Object[]> results = orderRepository.getRevenueByUser();
        List<RevenueReportResponse.RevenueData> dataList = new ArrayList<>();

        for (Object[] result : results) {
            String username = (String) result[0];
            String fullName = (String) result[1];
            Long totalOrders = ((Number) result[2]).longValue();
            Double revenue = ((Number) result[3]).doubleValue();

            RevenueReportResponse.RevenueData data = RevenueReportResponse.RevenueData.builder()
                    .label(username)
                    .description(fullName != null ? fullName : "N/A")
                    .revenue(Math.round(revenue * 100.0) / 100.0)
                    .count(totalOrders)
                    .build();

            dataList.add(data);
        }

        return dataList;
    }

    private List<RevenueReportResponse.RevenueData> getRevenueByProduct() {
        List<Object[]> results = orderRepository.getRevenueByProduct();
        List<RevenueReportResponse.RevenueData> dataList = new ArrayList<>();

        for (Object[] result : results) {
            String productName = (String) result[0];
            Long totalQuantity = ((Number) result[1]).longValue();
            Double revenue = ((Number) result[2]).doubleValue();

            RevenueReportResponse.RevenueData data = RevenueReportResponse.RevenueData.builder()
                    .label(productName)
                    .description("Sold: " + totalQuantity + " units")
                    .revenue(Math.round(revenue * 100.0) / 100.0)
                    .quantity(totalQuantity)
                    .build();

            dataList.add(data);
        }

        return dataList;
    }
}