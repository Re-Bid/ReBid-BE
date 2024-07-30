package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.rebid.entity.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
