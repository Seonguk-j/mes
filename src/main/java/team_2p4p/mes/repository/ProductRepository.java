package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByItemItemId(Long itemId);
}
