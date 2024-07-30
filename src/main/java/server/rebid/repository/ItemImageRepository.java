package server.rebid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.rebid.entity.ItemImage;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long>{
}
