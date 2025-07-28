package ra.java_service_15.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.java_service_15.model.entity.Review;
import ra.java_service_15.model.entity.User;
import ra.java_service_15.model.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Tìm tất cả reviews của một sản phẩm
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.id = :productId ORDER BY r.createdDate DESC")
    List<Review> findByProductIdOrderByCreatedDateDesc(@Param("productId") Long productId);

    /**
     * Tìm tất cả reviews của một sản phẩm có phân trang
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.id = :productId")
    Page<Review> findByProductId(@Param("productId") Long productId, Pageable pageable);

    /**
     * Kiểm tra user đã review sản phẩm này chưa
     */
    Optional<Review> findByUserAndProduct(User user, Product product);

    /**
     * Kiểm tra user đã review sản phẩm này chưa (by IDs)
     */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    Optional<Review> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Đếm số lượng reviews của một sản phẩm
     */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);

    /**
     * Tính rating trung bình của một sản phẩm
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    /**
     * Lấy tất cả reviews của một user
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.product WHERE r.user.id = :userId ORDER BY r.createdDate DESC")
    List<Review> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);
}