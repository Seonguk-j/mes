package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Enterprise;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {

//    void deleteByItemCode(String cp);
//
//    Item findByItemName(String name);
}
