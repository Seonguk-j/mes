package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Material;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    public List<Material> findByItemItemId(Long itemId);
}
