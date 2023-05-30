package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import team_2p4p.mes.entity.Product;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Nullable
    List<Product> findByItemItemId(Long itemId);

//    @Query(value = "select * from product where Date(make_date) >= :today and export_stat = 0", nativeQuery = true)
//    List<Product> statZeroToOne(@Param("today") LocalDate today);
}
