package ra.java_service_15.service;

import ra.java_service_15.model.dto.response.RevenueReportResponse;

public interface ReportService {


    RevenueReportResponse getRevenueReport(String type);
}