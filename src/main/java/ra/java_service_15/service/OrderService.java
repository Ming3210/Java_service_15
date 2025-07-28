package ra.java_service_15.service;



import ra.java_service_15.model.dto.request.OrderRequest;
import ra.java_service_15.model.entity.Order;

import java.util.List;
public interface OrderService {
    Order createOrder(OrderRequest request, String username);
    List<Order> getMyOrders(String username);
    List<Order> getAllOrders();
}
