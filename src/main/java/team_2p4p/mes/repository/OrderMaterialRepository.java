package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team_2p4p.mes.entity.OrderMaterial;
import team_2p4p.mes.entity.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
    List<OrderMaterial> findByItemItemId(Long itemId);
    @Query(value = "select * from order_material o where date(o.order_date) = :orderDate", nativeQuery = true)
    List<OrderMaterial> findByOrderDate(@Param("orderDate") LocalDate orderDate);

}
