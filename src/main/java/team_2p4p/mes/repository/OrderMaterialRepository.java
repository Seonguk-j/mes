package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.OrderMaterial;

public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
}
