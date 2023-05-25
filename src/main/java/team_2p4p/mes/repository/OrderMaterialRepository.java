package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.OrderMaterial;
import team_2p4p.mes.entity.Product;

import java.time.LocalDate;
import java.util.List;

public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
    List<OrderMaterial> findByItemItemId(Long itemId);
    List<OrderMaterial> findByOrderDate(LocalDate orderDate);

}
