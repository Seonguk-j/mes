package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import team_2p4p.mes.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Nullable
    List<Product> findByItemItemId(Long itemId);
}
