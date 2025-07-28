package ra.java_service_15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.java_service_15.model.entity.Order;
import ra.java_service_15.model.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(od) > 0 THEN true ELSE false END " +
            "FROM OrderDetail od JOIN od.order o " +
            "WHERE o.user.id = :userId AND od.product.id = :productId")
    Boolean hasUserPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId);


    @Query("SELECT SUM(od.quantity * od.product.price) " +
            "FROM OrderDetail od")
    Double getTotalRevenue();


    @Query("SELECT o.orderId, o.user.username, " +
            "SUM(od.quantity * od.product.price) as totalRevenue " +
            "FROM Order o JOIN o.orderDetails od " +
            "GROUP BY o.orderId, o.user.username " +
            "ORDER BY o.orderId")
    List<Object[]> getRevenueByOrder();


    @Query("SELECT o.user.username, o.user.fullName, " +
            "COUNT(o.orderId) as totalOrders, " +
            "SUM(od.quantity * od.product.price) as totalRevenue " +
            "FROM Order o JOIN o.orderDetails od " +
            "GROUP BY o.user.username, o.user.fullName " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getRevenueByUser();


    @Query("SELECT od.product.productName, " +
            "SUM(od.quantity) as totalQuantity, " +
            "SUM(od.quantity * od.product.price) as totalRevenue " +
            "FROM OrderDetail od " +
            "GROUP BY od.product.id, od.product.productName " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getRevenueByProduct();
}