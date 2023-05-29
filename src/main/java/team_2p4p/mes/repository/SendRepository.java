package team_2p4p.mes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team_2p4p.mes.entity.Bom;
import team_2p4p.mes.entity.Send;

public interface SendRepository extends JpaRepository<Send, Long> {
}
