package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.rebid.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Optional<Category> findByName(String categoryName);
}
