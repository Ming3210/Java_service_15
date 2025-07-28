package ra.java_service_15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.java_service_15.model.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
