package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import team_2p4p.mes.entity.Material;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Nullable
    public List<Material> findByItemItemId(Long itemId);

    @Query(value = "select * from material m where m.material_stat = :materialStat and m.item_id = :itemId", nativeQuery = true)
    public List<Material> findByMaterialStatAndItemId(@Param("materialStat") int materialStat, @Param("itemId") Long itemId);
}
